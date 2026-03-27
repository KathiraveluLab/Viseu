package edu.emory.viseu.overlay;

import edu.emory.viseu.overlay.messaging.P2PMessage;
import edu.emory.viseu.overlay.model.Peer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket-based server for receiving P2P messages in the Viseu overlay.
 */
public class OverlayServer extends Thread {
    private static final Logger logger = LogManager.getLogger(OverlayServer.class);
    private final int port;
    private final PeerRegistry registry;
    private volatile boolean running = true;

    public OverlayServer(int port) {
        this.port = port;
        this.registry = PeerRegistry.getInstance();
    }

    public void stopServer() {
        running = false;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Overlay server started on port: " + port);
            while (running) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    
                    P2PMessage message = (P2PMessage) in.readObject();
                    handleMessage(message);
                    
                } catch (IOException | ClassNotFoundException e) {
                    if (running) {
                        logger.error("Error handling incoming message", e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Could not start overlay server on port " + port, e);
        }
    }

    private void handleMessage(P2PMessage message) {
        logger.info("Received message: " + message.getType() + " from " + message.getSenderId());
        switch (message.getType()) {
            case JOIN:
                Peer newPeer = (Peer) message.getPayload();
                registry.registerPeer(newPeer);
                break;
            case HEARTBEAT:
                Peer p = registry.getPeer(message.getSenderId());
                if (p != null) {
                    p.updateHeartbeat();
                }
                break;
            case LEAVE:
                registry.removePeer(message.getSenderId());
                break;
            default:
                logger.warn("Unhandled message type: " + message.getType());
        }
    }
}
