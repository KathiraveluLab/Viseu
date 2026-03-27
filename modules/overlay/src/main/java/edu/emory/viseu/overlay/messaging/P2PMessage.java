package edu.emory.viseu.overlay.messaging;

import java.io.Serializable;

/**
 * P2P Message definition for Viseu.
 */
public class P2PMessage implements Serializable {
    public enum Type { JOIN, LEAVE, HEARTBEAT, DISCOVER, UPDATE_PEERS }

    private final Type type;
    private final String senderId;
    private final Object payload;

    public P2PMessage(Type type, String senderId, Object payload) {
        this.type = type;
        this.senderId = senderId;
        this.payload = payload;
    }

    public Type getType() { return type; }
    public String getSenderId() { return senderId; }
    public Object getPayload() { return payload; }
}
