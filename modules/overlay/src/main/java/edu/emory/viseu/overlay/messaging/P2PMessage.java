package edu.emory.viseu.overlay.messaging;

import edu.emory.viseu.overlay.model.Peer;
import java.io.Serializable;

/**
 * P2P Message definition for Viseu.
 */
public class P2PMessage implements Serializable {
    public enum Type { JOIN, HEARTBEAT, LEAVE, DATA }

    private final Type type;
    private final String senderId;
    private final Peer payload;
    
    // Telemetry fields
    private double cpuLoad;
    private long memoryAvailable;

    public P2PMessage(Type type, String senderId, Peer payload) {
        this.type = type;
        this.senderId = senderId;
        this.payload = payload;
        if (payload != null) {
            this.cpuLoad = payload.getCpuLoad();
            this.memoryAvailable = payload.getMemoryAvailable();
        }
    }

    public Type getType() { return type; }
    public String getSenderId() { return senderId; }
    public Peer getPayload() { return payload; }
    public double getCpuLoad() { return cpuLoad; }
    public long getMemoryAvailable() { return memoryAvailable; }
}
