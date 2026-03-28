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
        peer.setReputation(Math.min(10.0, currentRep + 0.1)); // Cap reputation at 10.0
        logger.info("Rewarded peer " + peer.getId() + ". New reputation: " + peer.getReputation());
    }

    /**
     * Penalize peer reputation for SLA violations.
     */
    public static void penalizePeer(Peer peer) {
        double currentRep = peer.getReputation();
        peer.setReputation(Math.max(0.1, currentRep - 0.5)); // Floor reputation at 0.1
        logger.warn("Penalized peer " + peer.getId() + ". New reputation: " + peer.getReputation());
    }
}
