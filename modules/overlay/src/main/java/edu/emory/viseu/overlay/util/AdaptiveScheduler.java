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

    // Weights for the selection formula
    private static final double WEIGHT_TRUST = 0.6;
    private static final double WEIGHT_LATENCY = 0.4;

    // Cluster-wide latency tracker for adaptive thresholding
    private static final LatencyTracker clusterLatencyTracker = new LatencyTracker();

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
            double latencyBenefit = (latency > 0) ? (100.0 / latency) : 0; 
            double cpuPenalty = peer.getCpuLoad() * 50.0; 
            double finalScore = (trustScore * WEIGHT_TRUST) + (latencyBenefit * WEIGHT_LATENCY) - (cpuPenalty * 0.2);

            // Record latency into the cluster tracker for future P95 calculations
            clusterLatencyTracker.recordLatency(latency);

            logger.info(String.format("Peer %s Score: %.2f (Trust: %.2f, Latency: %.2fms)", 
                peer.getId(), finalScore, trustScore, latency));

            if (finalScore > highestScore) {
                highestScore = finalScore;
                bestPeer = peer;
            }
        }

        // Adaptive Thresholding logic as per Viseu paper:
        // Trigger Cloud-Bursting if the best peer's latency exceeds the cluster P95.
        // Also enforcing a minimum trust score.
        double p95Threshold = clusterLatencyTracker.getP95();
        double bestPeerLatency = (bestPeer != null) ? simulateLatency(bestPeer.getIp()) : Double.MAX_VALUE;

        if (bestPeer != null && bestPeerLatency <= p95Threshold && highestScore > 2.0) {
            logger.info(String.format("Adaptive Scheduler selected Peer: %s (Latency: %.2fms <= P95: %.2fms)", 
                bestPeer.getId(), bestPeerLatency, p95Threshold));
            return Optional.of(bestPeer);
        } else {
            if (bestPeer != null) {
                if (bestPeerLatency > p95Threshold) {
                    logger.warn(String.format("Best peer latency (%.2fms) exceeds P95 threshold (%.2fms). Bursting to CLOUD.", 
                        bestPeerLatency, p95Threshold));
                } else {
                    logger.warn("Best edge peer trust/score (" + highestScore + ") is below threshold. Bursting to CLOUD.");
                }
            } else {
                logger.warn("No edge peers available. Bursting to CLOUD.");
            }
            return Optional.empty(); 
        }
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
