package edu.emory.viseu.overlay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility to track a sliding window of latencies and compute percentiles.
 * Used for the Adaptive Thresholding logic in the Viseu framework.
 */
public class LatencyTracker {
    private static final int MAX_SAMPLES = 100;
    private final List<Double> samples = new ArrayList<>();

    /**
     * Records a new latency measurement.
     * @param latency Latency value in milliseconds.
     */
    public synchronized void recordLatency(double latency) {
        if (samples.size() >= MAX_SAMPLES) {
            samples.remove(0); // Evict oldest sample
        }
        samples.add(latency);
    }

    /**
     * Calculates the P95 (95th percentile) of the recorded latencies.
     * @return P95 value in milliseconds. Returns 100.0 (default) if insufficient data.
     */
    public synchronized double getP95() {
        if (samples.isEmpty()) {
            return 100.0; // Default reasonable threshold
        }

        List<Double> sortedSamples = new ArrayList<>(samples);
        Collections.sort(sortedSamples);

        int index = (int) Math.ceil(0.95 * sortedSamples.size()) - 1;
        return sortedSamples.get(Math.max(0, index));
    }

    public synchronized int getSampleSize() {
        return samples.size();
    }
}
