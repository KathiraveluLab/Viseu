package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.OverlayManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Execute the core workflow
 */
public class ViseuExecutor {
	private static Logger logger = LogManager.getLogger(ViseuExecutor.class.getName());

	public static void execute() {
		long startTime = System.currentTimeMillis();

		// Initialize P2P Overlay
		int overlayPort = 5005; // Default overlay port
		OverlayManager overlayManager = new OverlayManager(overlayPort);
		overlayManager.start();

		logger.info("Viseu Overlay started on port " + overlayPort);

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);

		logger.info("The execution took: " + duration / 1000.0 + " seconds.");

	}
}
