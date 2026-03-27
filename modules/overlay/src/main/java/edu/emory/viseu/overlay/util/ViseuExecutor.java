package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.OverlayManager;
import edu.emory.viseu.overlay.WorkflowEngine;
import edu.emory.viseu.overlay.model.Task;
import edu.emory.viseu.overlay.model.Workflow;
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
		logger.info("Starting Viseu Overlay Service...");
        OverlayManager.getInstance().start();

        // Demonstration of Decentralized Workflow Engine (Task 6)
        logger.info("Triggering Decentralized Workflow demo...");
        
        Workflow workflow = new Workflow("MedicalImageAnalysis");
        
        Task preProcess = new Task("T1", "DicomPreprocessing");
        Task classification = new Task("T2", "ImageNetClassification");
        classification.addDependency("T1");
        Task storage = new Task("T3", "ResultStorage");
        storage.addDependency("T2");

        workflow.addTask(preProcess);
        workflow.addTask(classification);
        workflow.addTask(storage);

        WorkflowEngine engine = new WorkflowEngine();
        engine.run(workflow);

        // Automated Benchmarking (Task 4)
        logger.info("Starting automated benchmark (3 iterations)...");
        new ViseuBenchmark().runBenchmark(3);
		
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);

		logger.info("The execution took: " + duration / 1000.0 + " seconds.");

	}
}
