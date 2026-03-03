package com.honeypot.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents information about a network connection attempt.
 * This class encapsulates all relevant data about an incoming connection
 * to the honeypot, including source/destination information and timing.
 * 
 * @author Elodie Moisan
 * @version 1.0.0
 */

public class ConnectionInfo {

    private final String sourceIP;
    private final int sourcePort;
    private final int destinationPort;
    private final String protocol;
    private final LocalDateTime timestamp;
    private final String serviceName;

    /**
     * Constructor for ConnectionInfo
     * 
     * @param sourceIP The IP address of the connecting client 
     * @param sourcePort The port number used by the client
     * @param destinationPort The port number of the honeypot service
        * @param protocol The protocol used (TCP, UDP, etc.)
        * @param serviceName The name of the service being targeted (SSH, HTTP, etc.)
     */

    public ConnectionInfo(String sourceIP, int sourcePort, int destinationPort, String protocol, String serviceName){
        this.sourceIP = sourceIP;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.timestamp = LocalDateTime.now();
        this.serviceName = serviceName;
    }

    // Getters
    public String getSourceIP(){
        return sourceIP;
    }

    public int getSourcePort(){
        return sourcePort;
    }

    public int getDestinationPort(){
        return destinationPort;
    }

    public String getProtocol(){
        return protocol;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public String getServiceName(){
        return serviceName;
    }

    /**
     * Returns a human-readable string representation of the connection
     * 
     * @return Formatted connection information
     */

    @Override
    public String toString(){
        return String.format(
            "Connection from %s:%d to %s (port %d) via %s at %s",
            sourceIP, sourcePort, serviceName, destinationPort, protocol, timestamp
        );
    }

    /**
     * Checks equality based on all fields 
     * 
     * @param o Object to compare with
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } 

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ConnectionInfo that = (ConnectionInfo) o;
        return sourcePort == that.sourcePort &&
                destinationPort == that.destinationPort &&
                Objects.equals(sourceIP, that.sourceIP) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(timestamp, that.timestamp);
    }

    /**
     * Generates a hash code based on all fields
     * 
     * @return Hash code
     */
    @Override
    public int hashCode(){
        return Objects.hash(sourceIP, sourcePort, destinationPort, protocol, serviceName, timestamp);
    }

}
