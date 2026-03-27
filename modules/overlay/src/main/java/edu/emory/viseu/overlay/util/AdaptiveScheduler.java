package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.PeerRegistry;
import edu.emory.viseu.overlay.model.Peer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Adaptive Task Scheduler for Viseu.
 * Unifies P2P nodes, PoT scores, and Latency measurements to select the optimal peer.
 */
public class AdaptiveScheduler {
    private static final Logger logger = LogManager.getLogger(AdaptiveScheduler.class);

    // Weights for the selection formula (can be tuned or moved to constants)
    private static final double WEIGHT_TRUST = 0.6;
    private static final double WEIGHT_LATENCY = 0.4;

    /**
     * Finds the best peer for a given service request.
     * 
     * @param serviceName Name of the service requested.
     * @return The optimal peer selected by the adaptive algorithm.
     */
    public static Optional<Peer> findBestPeer(String serviceName) {
        java.util.Collection<Peer> candidates = PeerRegistry.getInstance().getAllPeers();
        
        if (candidates.isEmpty()) {
            logger.warn("No candidate peers found for service: " + serviceName);
            return Optional.empty();
        }

        Peer bestPeer = null;
        double highestScore = -1.0;

        for (Peer peer : candidates) {
            // 1. Calculate Trust Score (PoT)
            double trustScore = TrustScoreCalculator.calculateTrust(peer);

            // 2. Obtain Latency (Real-time or cached)
            // In this implementation, we simulate the latency value. 
            // In a production environment, this would call the Python MeasurementClient API.
            double latency = simulateLatency(peer.getIp());

            // 3. Compute Weighted Score
            // Score = (Trust * Wt) + ((100/Latency) * Wl) - (CPULoad * 50 * Wc)
            // Note: 1/latency favors lower values. Lower CPU load is also better.
            double latencyBenefit = (latency > 0) ? (100.0 / latency) : 0; 
            double cpuPenalty = peer.getCpuLoad() * 50.0; // Assume 0.0 to 1.0, scale to 50
            double finalScore = (trustScore * WEIGHT_TRUST) + (latencyBenefit * WEIGHT_LATENCY) - (cpuPenalty * 0.2);

            logger.info(String.format("Peer %s Score: %.2f (Trust: %.2f, Latency: %.2fms)", 
                peer.getId(), finalScore, trustScore, latency));

            if (finalScore > highestScore) {
                highestScore = finalScore;
                bestPeer = peer;
            }
        }

        if (bestPeer != null) {
            logger.info("Adaptive Scheduler selected Peer: " + bestPeer.getId() + " with score: " + highestScore);
        }

        return Optional.ofNullable(bestPeer);
    }

    /**
     * Mocks a latency measurement for a target IP.
     * Integrating with MeasurementClient.py via REST would go here.
     */
    private static double simulateLatency(String ip) {
        // Return a randomized latency between 10ms and 150ms for demonstration
        return 10.0 + (Math.random() * 140.0);
    }
}
