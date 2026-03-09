package com.honeypot.core;

import com.honeypot.models.AttackLog;
import com.honeypot.models.ConnectionInfo;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Asynchronous logger for honeypot attack logs.
 * Uses a separate thread to write logs to disk without blocking
 * the main honeypot services.
 * 
 * Thread-safe and designed for high-throughput logging.
 * 
 * @author Elodie Moisan
 * @version 1.0.0
 */
public class Logger implements AutoCloseable {
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);
    
    private final String logDirectory;
    private final BlockingQueue<AttackLog> logQueue;
    private final Thread loggerThread;
    private final AtomicBoolean running;
    private final AtomicLong totalLogsWritten;
    
    private static final int QUEUE_CAPACITY = 10000;
    private static final DateTimeFormatter FILE_DATE_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Creates a new Logger instance
     * 
     * @param logDirectory Directory where log files will be stored
     * @throws IOException if the log directory cannot be created
     */
    public Logger(String logDirectory) throws IOException {
        this.logDirectory = logDirectory;
        this.logQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.running = new AtomicBoolean(false);
        this.totalLogsWritten = new AtomicLong(0);
        
        // Create log directory if it doesn't exist
        Path logPath = Paths.get(logDirectory);
        if (!Files.exists(logPath)) {
            Files.createDirectories(logPath);
            log.info("Created log directory: {}", logDirectory);
        }
        
        // Initialize the logging thread
        this.loggerThread = new Thread(this::processLogs, "HoneypotLogger");
        this.loggerThread.setDaemon(true);
    }
    
    /**
     * Starts the logger thread
     */
    public void start() {
        if (running.compareAndSet(false, true)) {
            loggerThread.start();
            log.info("Logger started - writing logs to: {}", logDirectory);
        }
    }
    
    /**
     * Logs a connection attempt
     * 
     * @param connectionInfo The connection information to log
     */
    public void logConnection(ConnectionInfo connectionInfo) {
        log.info("Connection attempt: {}", connectionInfo);
        
        // Create basic attack log from connection info
        AttackLog attackLog = AttackLog.fromConnectionInfo(connectionInfo)
                .attackType("connection_attempt")
                .build();
        
        logAttack(attackLog);
    }
    
    /**
     * Logs a detected attack
     * 
     * @param attackLog The attack log to write
     */
    public void logAttack(AttackLog attackLog) {
        if (!running.get()) {
            log.warn("Logger is not running, attack log will not be persisted");
            return;
        }
        
        try {
            // Non-blocking offer with timeout
            boolean added = logQueue.offer(attackLog);
            if (!added) {
                log.error("Log queue is full! Dropping attack log: {}", attackLog.getId());
            } else {
                log.debug("Attack queued for logging: {}", attackLog);
            }
        } catch (Exception e) {
            log.error("Failed to queue attack log: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Main logging loop - runs in separate thread
     * Continuously processes logs from the queue and writes them to disk
     */
    private void processLogs() {
        log.info("Logger thread started");
        
        while (running.get() || !logQueue.isEmpty()) {
            try {
                // Wait for logs (blocks if queue is empty)
                AttackLog attackLog = logQueue.poll(
                    100, 
                    java.util.concurrent.TimeUnit.MILLISECONDS
                );
                
                if (attackLog != null) {
                    writeLogToDisk(attackLog);
                    totalLogsWritten.incrementAndGet();
                }
                
            } catch (InterruptedException e) {
                log.info("Logger thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error processing log: {}", e.getMessage(), e);
            }
        }
        
        log.info("Logger thread stopped. Total logs written: {}", totalLogsWritten.get());
    }
    
    /**
     * Writes an attack log to a JSON file
     * 
     * @param attackLog The attack log to write
     */
    private void writeLogToDisk(AttackLog attackLog) {
        try {
            // Generate unique filename with timestamp
            String timestamp = attackLog.getTimestamp().format(FILE_DATE_FORMAT);
            String filename = String.format(
                "%s_%s_%s.json",
                attackLog.getServiceName().toLowerCase(),
                timestamp,
                attackLog.getId().substring(0, 8)
            );
            
            File logFile = new File(logDirectory, filename);
            
            // Write JSON to file
            try (FileWriter writer = new FileWriter(logFile)) {
                writer.write(attackLog.toJson());
            }
            
            log.debug("Attack log written to: {}", logFile.getAbsolutePath());
            
        } catch (IOException e) {
            log.error("Failed to write attack log to disk: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Gets the total number of logs written
     * 
     * @return Total logs written since logger started
     */
    public long getTotalLogsWritten() {
        return totalLogsWritten.get();
    }
    
    /**
     * Gets the current size of the log queue
     * 
     * @return Number of logs waiting to be written
     */
    public int getQueueSize() {
        return logQueue.size();
    }
    
    /**
     * Checks if the logger is running
     * 
     * @return true if logger is active
     */
    public boolean isRunning() {
        return running.get();
    }
    
    /**
     * Stops the logger and waits for all queued logs to be written
     * 
     * @throws InterruptedException if interrupted while waiting
     */
    public void stop() throws InterruptedException {
        if (running.compareAndSet(true, false)) {
            log.info("Stopping logger... Queue size: {}", logQueue.size());
            
            // Wait for the logger thread to finish
            loggerThread.join(5000); // Wait max 5 seconds
            
            if (loggerThread.isAlive()) {
                log.warn("Logger thread did not stop gracefully");
            } else {
                log.info("Logger stopped successfully. Total logs: {}", totalLogsWritten.get());
            }
        }
    }
    
    /**
     * Implements AutoCloseable for try-with-resources
     */
    @Override
    public void close() {
        try {
            stop();
        } catch (InterruptedException e) {
            log.error("Interrupted while closing logger", e);
            Thread.currentThread().interrupt();
        }
    }
}
