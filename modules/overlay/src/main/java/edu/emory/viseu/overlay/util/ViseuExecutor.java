package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.OverlayManager;
import edu.emory.viseu.overlay.model.Peer;
import edu.emory.viseu.overlay.util.AdaptiveScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Execute the core workflow
 */
public class ViseuExecutor {
	private static Logger logger = LogManager.getLogger(ViseuExecutor.class.getName());

	public static void execute() {
		long startTime = System.currentTimeMillis();

		// Initialize P2P Overlay
		logger.info("Starting Viseu Overlay Service...");
        OverlayManager.getInstance().start();

        // Demonstration of Adaptive Scheduler (Task 5)
        logger.info("Triggering Adaptive Scheduler demo...");
        Optional<Peer> bestPeer = AdaptiveScheduler.findBestPeer("ImageNetClassification");
        
        if (bestPeer.isPresent()) {
            logger.info("Optimized Selection: Peer " + bestPeer.get().getId() + " is the primary candidate for offloading.");
        } else {
            logger.warn("Scheduler could not identify a primary candidate. Operation will continue locally.");
        }
		
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);

		logger.info("The execution took: " + duration / 1000.0 + " seconds.");

	}
}
