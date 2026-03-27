package edu.emory.viseu.overlay;

import edu.emory.viseu.overlay.messaging.P2PMessage;
import edu.emory.viseu.overlay.model.Peer;
import edu.emory.viseu.overlay.util.ResourceMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * Orchestrates the lifecycle of the Viseu P2P overlay.
 */
public class OverlayManager {
    private static final Logger logger = LogManager.getLogger(OverlayManager.class);
    private static OverlayManager instance;
    private final String nodeId;
    private final int port;
    private final OverlayServer server;
    private final OverlayClient client;
    private final Peer self;
    private final ResourceMonitor resourceMonitor;

    private OverlayManager(int port) {
        this.nodeId = "viseu-node-" + UUID.randomUUID().toString().substring(0, 8);
        this.port = port;
        this.server = new OverlayServer(port);
        this.client = new OverlayClient();
        this.self = new Peer(nodeId, "127.0.0.1", port);
        this.resourceMonitor = new ResourceMonitor();
    }

    public static synchronized OverlayManager getInstance() {
        if (instance == null) {
            instance = new OverlayManager(5005); // Default port
        }
        return instance;
    }

    public void start() {
        logger.info("Starting Overlay Management for node " + nodeId + " on port " + port);
        server.start();
        resourceMonitor.start();
        logger.info("Overlay Node initialized: " + self);
    }

    public void stop() {
        server.stopServer();
        logger.info("Overlay Node stopped: " + nodeId);
    }

    public void broadcastJoin(Peer bootstrapPeer) {
        P2PMessage joinMsg = new P2PMessage(P2PMessage.Type.JOIN, nodeId, self);
        client.sendMessage(bootstrapPeer, joinMsg);
    }

    public String getNodeId() {
        return nodeId;
    }

    public void updateSelfResources(double cpuLoad, long memAvailable) {
        if (self != null) {
            self.setCpuLoad(cpuLoad);
            self.setMemoryAvailable(memAvailable);
        }
    }
}
