package com.honeypot.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a complete attack log entry.
 * This class contains all information about a detected intrusion attempt,
 * including connection details, payload data, and attack classification.
 * 
 * @author Elodie Moisan
 * @version 1.0.0
 */

public class AttackLog {

    private final String id;
    private final LocalDateTime timestamp;
    private final String sourceIP;
    private final int sourcePort;
    private final int destinationPort;
    private final String serviceName;
    private final String protocol;
    private final String attackType;
    private final String payload;
    private final int payloadSize;
    private final String userAgent;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Private constructor for builder pattern
     */
    private AttackLog(Builder builder){
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.sourceIP = builder.sourceIP;
        this.sourcePort = builder.sourcePort;
        this.destinationPort = builder.destinationPort;
        this.serviceName = builder.serviceName;
        this.protocol = builder.protocol;
        this.attackType = builder.attackType;
        this.payload = builder.payload;
        this.payloadSize = builder.payload != null ? builder.payload.length() : 0;
        this.userAgent = builder.userAgent;
    }

    //Getters
    public String getId(){
        return id;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public String getSourceIP(){
        return sourceIP;
    }

    public int getSourcePort(){
        return sourcePort;
    }

    public int getDestinationPort(){
        return destinationPort;
    }

    public String getServiceName(){
        return serviceName;
    }

    public String getProtocol(){
        return protocol;
    }

    public String getAttackType(){
        return attackType;
    }

    public String getPayload(){
        return payload;
    }

    public int getPayloadSize(){
        return payloadSize;
    }

    public String getUserAgent(){
        return userAgent;
    }

    /**
     * Converts this AttackLog to JSON format
     * 
     * @return JSON string representation
     */
    public String toJson(){
        return gson.toJson(this);
    }

    /**
     * Creates an AttackLog from ConnectionInfo 
     * 
     * @param connectionInfo The connection information
        * @return Builder instance for further customization
     */
    public static Builder fromConnectionInfo(ConnectionInfo connectionInfo){
        return new Builder()
                .sourceIP(connectionInfo.getSourceIP())
                .sourcePort(connectionInfo.getSourcePort())
                .destinationPort(connectionInfo.getDestinationPort())
                .serviceName(connectionInfo.getServiceName())
                .protocol(connectionInfo.getProtocol());
    }

    @Override
    public String toString(){
        return String.format("[%s] Attack from %s:%d to %s (port %d) - Type: %s, Payload: %d bytes",
            timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            sourceIP, sourcePort, serviceName, destinationPort, 
            attackType, payloadSize
        );
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttackLog attackLog = (AttackLog) o;
        return Objects.equals(id, attackLog.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    /**
     * Builder class for AttackLog following the Builder pattern
     */
    public static class Builder{
        private String sourceIP;
        private int sourcePort;
        private int destinationPort;
        private String serviceName;
        private String protocol = "TCP";
        private String attackType = "unknown";
        private String payload;
        private String userAgent;

        public Builder sourceIP(String sourceIP) {
            this.sourceIP = sourceIP;
            return this;
        }
        
        public Builder sourcePort(int sourcePort) {
            this.sourcePort = sourcePort;
            return this;
        }
        
        public Builder destinationPort(int destinationPort) {
            this.destinationPort = destinationPort;
            return this;
        }
        
        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }
        
        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }
        
        public Builder attackType(String attackType) {
            this.attackType = attackType;
            return this;
        }
        
        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        /**
         * Builds the AttackLog instance 
         * 
         * @return New AttackLog instance
         * @throws IllegalStateException if required fields are missing
         */
        public AttackLog build(){
            if (sourceIP == null || serviceName == null){
                throw new IllegalStateException(
                    "sourceIP and serviceName are required fields"
                );
            }
            return new AttackLog(this);
        }

    }

}
