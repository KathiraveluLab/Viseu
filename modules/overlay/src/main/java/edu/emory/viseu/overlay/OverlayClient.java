package edu.emory.viseu.overlay;

import edu.emory.viseu.overlay.messaging.P2PMessage;
import edu.emory.viseu.overlay.model.Peer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client for sending P2P messages to other peers in the Viseu overlay.
 */
public class OverlayClient {
    private static final Logger logger = LogManager.getLogger(OverlayClient.class);

    public void sendMessage(Peer target, P2PMessage message) {
        try (Socket socket = new Socket(target.getIp(), target.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            
            out.writeObject(message);
            out.flush();
            logger.debug("Sent " + message.getType() + " to " + target.getId());
            
        } catch (IOException e) {
            logger.error("Failed to send " + message.getType() + " to peer " + target.getId() + " at " + target.getIp() + ":" + target.getPort());
        }
    }
}
