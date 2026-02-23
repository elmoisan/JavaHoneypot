package com.honeypot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the JavaHoneypot application.
 * 
 * This honeypot system simulates a vulnerable network service to detect,
 * log, and analyze intrusion attempts for cybersecurity research and education.
 * 
 * @author Elodie Moisan
 * @version 1.0.0
 * @since 2026-02-23
 */

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String VERSION = "1.0.0-SNAPSHOT";

    /**
     * Main method - application entry point
     * 
     * @param args Command line arguments (future: config file path, services to enable, etc.)
     */
    public static void main(String[] args){
        printBanner();
        logger.info("JavaHoneypot v{} starting...", VERSION);

        try{
            // TODO: Initialize configuration
            // TODO: Create HoneypotServer instance
            // TODO: Start services
            // TODO: Setup shutdown hook     
            
            logger.info("JavaHoneypot started successfully!");
            logger.info("Press Ctrl+C to stop the honeypot");

            //Keep the application running 
            Thread.currentThread().join();

        } catch (InterruptedException e){
            logger.info("Honeypot interrupted, shutting down...");
            Thread.currentThread().interrupt();
        } catch(Exception e){
            logger.error("Fatal error occurred: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * Prints the application banner to console
     */
    private static void printBanner(){
        System.out.println("""
            
            ╔═══════════════════════════════════════════════════════════╗
            ║                                                           ║
            ║          🍯  J A V A   H O N E Y P O T  🍯                ║
            ║                                                           ║
            ║          Low-Interaction Honeypot System                  ║
            ║          Version: 1.0.0-SNAPSHOT                          ║
            ║                                                           ║
            ║          Author: Elodie Moisan                            ║
            ║          GitHub: github.com/elmoisan/JavaHoneypot         ║
            ║                                                           ║
            ╚═══════════════════════════════════════════════════════════╝
            
            """);
    }

}
