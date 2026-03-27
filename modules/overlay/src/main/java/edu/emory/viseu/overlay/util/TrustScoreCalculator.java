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

    /**
     * Calculates the Trust Score (T) for a given peer.
     * T = f(Reputation, ResourceCommitment, Time)
     * 
     * @param peer The peer to calculate trust for.
     * @return Calculated trust score.
     */
    public static double calculateTrust(Peer peer) {
        long now = System.currentTimeMillis();
        long age = (now - peer.getLastHeartbeat()) / 1000; // time in seconds since last contact

        // Base trust is the peer's reputation
        double reputation = peer.getReputation();

        // Time decay factor: Trust decreases if the peer hasn't been seen recently
        double timeDecay = Math.max(0.1, 1.0 - (age / 3600.0)); // decays over 1 hour

        double trustScore = reputation * timeDecay;

        logger.debug(String.format("Calculated trust for %s: %.2f (Rep: %.2f, Age: %ds)", 
            peer.getId(), trustScore, reputation, age));

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
