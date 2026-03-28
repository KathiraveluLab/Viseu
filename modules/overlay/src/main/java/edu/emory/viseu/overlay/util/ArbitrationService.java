package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.model.Peer;
import edu.emory.viseu.overlay.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Decentralized Arbitration Service for Viseu.
 * Manages dispute resolution and Proof of Trust (PoT) verification.
 */
public class ArbitrationService {
    private static final Logger logger = LogManager.getLogger(ArbitrationService.class);
    private static final double AUDIT_PROBABILITY = 0.1; // 10% random audit chance

    /**
     * Determines if a task result should be subjected to arbitration.
     * Decisions are based on peer trust scores and randomized probability.
     */
    public static boolean requiresArbitration(Peer peer, Task task) {
        double trust = TrustScoreCalculator.calculateTrust(peer);
        
        // Audit if trust is low OR by random chance as per Viseu paper.
        boolean lowTrustAudit = trust < 1.0;
        boolean randomAudit = Math.random() < AUDIT_PROBABILITY;

        return lowTrustAudit || randomAudit;
    }

    /**
     * Verifies a task result by selecting a high-trust Arbitrator peer.
     * 
     * @param originalPeer The peer that produced the initial result.
     * @param task The task in dispute.
     * @param originalResult The result to be verified.
     * @return True if the result is verified as correct; False if a dispute is triggered.
     */
    public static boolean verifyResult(Peer originalPeer, Task task, Object originalResult) {
        logger.info(String.format("Arbitration Triggered for Task %s (Original Peer: %s)", 
            task.getId(), originalPeer.getId()));

        // 1. Select an Arbitrator (High-trust peer)
        // In a full implementation, the scheduler selects a peer with T > higher_threshold.
        Optional<Peer> arbitrator = AdaptiveScheduler.findBestPeer(task.getServiceRequired());

        if (arbitrator.isPresent() && !arbitrator.get().getId().equals(originalPeer.getId())) {
            Peer arb = arbitrator.get();
            logger.info("Selected Arbitrator: " + arb.getId());

            // 2. Simulate re-execution by the Arbitrator
            Object arbResult = simulateArbitration(arb, task);

            // 3. Compare results
            if (originalResult.equals(arbResult)) {
                logger.info("Arbitration SUCCESS: Results match. Rewarding original peer.");
                TrustScoreCalculator.rewardPeer(originalPeer);
                return true;
            } else {
                logger.warn("Arbitration DISPUTE: Result mismatch detected. Penalizing original peer.");
                TrustScoreCalculator.penalizePeer(originalPeer);
                return false;
            }
        }

        // If no suitable arbitrator is found, default to trust (or cloud verification).
        logger.warn("No suitable arbitrator found. Task result accepted by default.");
        return true; 
    }

    private static Object simulateArbitration(Peer arbitrator, Task task) {
        // Arbitrators are assumed to be honest and highly reliable.
        return "Result of " + task.getServiceRequired() + " from " + arbitrator.getId();
    }
}
