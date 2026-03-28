package edu.emory.viseu.overlay.model;

import java.io.Serializable;

/**
 * Peer data model for the Viseu P2P overlay.
 */
public class Peer implements Serializable {
    private final String id;
    private final String ip;
    private final int port;
    // Proof of Trust (PoT) Components
    private double reputation; 
    private double stake;         // Proof of Stake (Wealth Component)
    private long lastHeartbeat;
 
    // Real-time Resource Metrics
    private double cpuLoad;       // 0.0 to 1.0
    private long memoryAvailable; // in MB
 
    public Peer(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.reputation = 0.5; // Default reputation
        this.stake = 1.0;      // Default initial stake
        this.lastHeartbeat = System.currentTimeMillis();
        this.cpuLoad = 0.0;
        this.memoryAvailable = 1024; // Default 1GB
    }
 
    public String getId() { return id; }
    public String getIp() { return ip; }
    public int getPort() { return port; }
    
    public double getReputation() { return reputation; }
    public void setReputation(double reputation) { this.reputation = reputation; }
 
    public double getStake() { return stake; }
    public void setStake(double stake) { this.stake = stake; }

    public double getCpuLoad() { return cpuLoad; }
    public void setCpuLoad(double cpuLoad) { this.cpuLoad = cpuLoad; }

    public long getMemoryAvailable() { return memoryAvailable; }
    public void setMemoryAvailable(long memoryAvailable) { this.memoryAvailable = memoryAvailable; }
    public long getLastHeartbeat() { return lastHeartbeat; }
    public void updateHeartbeat() { this.lastHeartbeat = System.currentTimeMillis(); }

    @Override
    public String toString() {
        return String.format("Peer{id='%s', address='%s:%d', reputation=%.2f}", id, ip, port, reputation);
    }
}
