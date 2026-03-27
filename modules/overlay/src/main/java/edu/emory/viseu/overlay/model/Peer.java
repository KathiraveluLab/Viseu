package edu.emory.viseu.overlay.model;

import java.io.Serializable;

/**
 * Peer data model for the Viseu P2P overlay.
 */
public class Peer implements Serializable {
    private final String id;
    private final String ip;
    private final int port;
    private double reputation;
    private long lastHeartbeat;

    public Peer(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.reputation = 1.0; // Initial trust
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getIp() { return ip; }
    public int getPort() { return port; }
    public double getReputation() { return reputation; }
    public void setReputation(double reputation) { this.reputation = reputation; }
    public long getLastHeartbeat() { return lastHeartbeat; }
    public void updateHeartbeat() { this.lastHeartbeat = System.currentTimeMillis(); }

    @Override
    public String toString() {
        return String.format("Peer{id='%s', address='%s:%d', reputation=%.2f}", id, ip, port, reputation);
    }
}
