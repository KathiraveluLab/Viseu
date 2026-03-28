package edu.emory.viseu.overlay;

import edu.emory.viseu.overlay.model.Peer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class PeerRegistryTest {

    private PeerRegistry registry;

    @Before
    public void setUp() throws ReflectiveOperationException {
        // Reset the singleton instance using reflection to ensure strict test isolation
        Field instanceField = PeerRegistry.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        registry = PeerRegistry.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        PeerRegistry instance1 = PeerRegistry.getInstance();
        PeerRegistry instance2 = PeerRegistry.getInstance();
        assertSame("Instances should be exactly the same object", instance1, instance2);
        assertNotNull("Instance should not be null", instance1);
    }

    @Test
    public void testRegisterAndGetPeer() {
        Peer peer = new Peer("peer1", "127.0.0.1", 8080);
        registry.registerPeer(peer);

        Peer retrievedPeer = registry.getPeer("peer1");
        assertNotNull("Retrieved peer should not be null", retrievedPeer);
        assertSame("Retrieved peer should be the same as the registered one", peer, retrievedPeer);
    }

    @Test
    public void testRemovePeer() {
        Peer peer = new Peer("peer2", "127.0.0.1", 8081);
        registry.registerPeer(peer);

        // Verify it's there
        assertNotNull(registry.getPeer("peer2"));

        registry.removePeer("peer2");

        // Verify it's gone
        assertNull("Peer should be null after removal", registry.getPeer("peer2"));
    }

    @Test
    public void testRemoveNonExistentPeer() {
        // Attempting to remove a peer that doesn't exist shouldn't throw an exception
        registry.removePeer("nonexistent_peer");
    }

    @Test
    public void testGetAllPeers() {
        Peer peer1 = new Peer("peer1", "127.0.0.1", 8080);
        Peer peer2 = new Peer("peer2", "127.0.0.1", 8081);
        Peer peer3 = new Peer("peer3", "127.0.0.1", 8082);

        registry.registerPeer(peer1);
        registry.registerPeer(peer2);
        registry.registerPeer(peer3);

        Collection<Peer> peers = registry.getAllPeers();
        assertNotNull("Peers collection should not be null", peers);
        assertEquals("Peers collection should contain exactly 3 peers", 3, peers.size());
java.util.Set<String> expectedIds = java.util.Set.of("peer1", "peer2", "peer3");
java.util.Set<String> actualIds = peers.stream().map(Peer::getId).collect(java.util.stream.Collectors.toSet());
assertEquals("Peers collection should contain exactly the registered peers", expectedIds, actualIds);
    }
}
