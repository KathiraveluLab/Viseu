package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.model.Peer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility to calculate Proof of Trust (PoT) scores for peers in the overlay.
 * Based on the Viseu research paper specifications.
 */
public class TrustScoreCalculator {
    private static final Logger logger = LogManager.getLogger(TrustScoreCalculator.class);

    // Decay constant (lambda) as specified in the Viseu research specifications.
    // Represents the temporal sensitivity of trust.
    private static final double LAMBDA = 0.0001; 

    /**
     * Calculates the Trust Score (T) for a given peer using temporal integration.
     * T = S * R * exp(-lambda * delta_t)
     * 
     * @param peer The peer to calculate trust for.
     * @return Calculated trust score aligned with Viseu architectural specifications.
     */
    public static double calculateTrust(Peer peer) {
        long now = System.currentTimeMillis();
        double deltaTime = (double) (now - peer.getLastHeartbeat()) / 1000.0; // Seconds

        double stake = peer.getStake();
        double reputation = peer.getReputation();

        // Implement the temporal exponential decay as specified in the Viseu paper.
        double temporalFactor = Math.exp(-LAMBDA * deltaTime);
        double trustScore = stake * reputation * temporalFactor;

        logger.debug(String.format("PoT Calculation for %s: Score=%.4f (S=%.2f, R=%.2f, dT=%.2fs)", 
            peer.getId(), trustScore, stake, reputation, deltaTime));

        return trustScore;
    }

    /**
     * Update peer reputation based on successful service delivery (PoT update).
     */
    public static void rewardPeer(Peer peer) {
        double currentRep = peer.getReputation();
        double newRep = Math.min(10.0, currentRep + 0.1); 
        peer.setReputation(newRep);
        
        syncToBlockchain(peer.getId(), newRep, "Service Success");
        logger.info("Rewarded peer " + peer.getId() + ". New reputation: " + newRep);
    }

    /**
     * Penalize peer reputation for SLA violations.
     */
    public static void penalizePeer(Peer peer) {
        double currentRep = peer.getReputation();
        double newRep = Math.max(0.1, currentRep - 0.5);
        peer.setReputation(newRep);
        
        syncToBlockchain(peer.getId(), newRep, "SLA Violation");
        logger.warn("Penalized peer " + peer.getId() + ". New reputation: " + newRep);
    }

    /**
     * Synchronizes reputation updates with the Viseu Ethereum bridge.
     * Uses a REST call to the Python API (app.py) to trigger on-chain PoT updates.
     */
    private static void syncToBlockchain(String peerId, double reputation, String reason) {
        logger.info(String.format("Synchronizing Proof of Trust [%s] for Peer %s to Blockchain (ViseuPoT.sol)", 
            reason, peerId));

        // Asynchronous REST call to avoid blocking the scheduler's decision loop
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL("http://localhost:5001/viseu/api/reputation");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String jsonInput = String.format("{\"peer_id\": \"%s\", \"new_reputation\": %.2f, \"reason\": \"%s\"}", 
                    peerId, reputation, reason);

                try (java.io.OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInput.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                if (code == 200) {
                    logger.info("Successfully anchored reputation for " + peerId + " on-chain.");
                } else {
                    logger.warn("Blockchain synchronization failed for " + peerId + " (Code: " + code + ")");
                }
            } catch (Exception e) {
                logger.error("Blockchain Bridge Error: " + e.getMessage());
            }
        }).start();
    }
}
