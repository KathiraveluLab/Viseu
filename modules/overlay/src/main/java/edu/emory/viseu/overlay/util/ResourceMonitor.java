package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.OverlayManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Periodically monitors system resources (CPU, RAM) and updates the local node's state.
 */
public class ResourceMonitor {
    private static final Logger logger = LogManager.getLogger(ResourceMonitor.class);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final OperatingSystemMXBean osBean;

    public ResourceMonitor() {
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
    }

    public void start() {
        logger.info("Starting Resource Monitor...");
        scheduler.scheduleAtFixedRate(this::updateMetrics, 0, 10, TimeUnit.SECONDS);
    }

    private void updateMetrics() {
        try {
            // Get System Load Average (approximation for CPU Load on standard MXBean)
            double cpuLoad = osBean.getSystemLoadAverage();
            if (cpuLoad < 0) cpuLoad = 0.5; // Fallback if not supported

            // Get Free Physical Memory (Requires casting to specialized bean for more detail, but using standard for portability)
            // Get Free Physical Memory (Approximation)
            long freeSwap = 512; // Placeholder for free memory in MB
            
            logger.debug(String.format("Local Metrics Update - CPU Load: %.2f, Mem: %dMB", cpuLoad, freeSwap));
            
            // Update local node state in OverlayManager
            OverlayManager.getInstance().updateSelfResources(cpuLoad, freeSwap);
        } catch (Exception e) {
            logger.error("Failed to update resource metrics", e);
        }
    }

    public void stop() {
        scheduler.shutdown();
    }
}
