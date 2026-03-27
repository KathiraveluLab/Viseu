package edu.emory.viseu.overlay;

import edu.emory.viseu.overlay.model.Peer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton registry for tracking peers in the Viseu overlay.
 */
public class PeerRegistry {
    private static final Logger logger = LogManager.getLogger(PeerRegistry.class);
    private static PeerRegistry instance;
    private final ConcurrentHashMap<String, Peer> peers = new ConcurrentHashMap<>();

    private PeerRegistry() {}

    public static synchronized PeerRegistry getInstance() {
        if (instance == null) {
            instance = new PeerRegistry();
        }
        return instance;
    }

    public void registerPeer(Peer peer) {
        peers.put(peer.getId(), peer);
        logger.info("Registered peer: " + peer);
    }

    public void removePeer(String peerId) {
        Peer removed = peers.remove(peerId);
        if (removed != null) {
            logger.info("Removed peer: " + removed);
        }
    }

    public Peer getPeer(String peerId) {
        return peers.get(peerId);
    }

    public Collection<Peer> getAllPeers() {
        return peers.values();
    }
}
