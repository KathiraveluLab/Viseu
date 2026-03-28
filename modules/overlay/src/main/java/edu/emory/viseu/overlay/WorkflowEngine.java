package edu.emory.viseu.overlay;

import edu.emory.viseu.overlay.model.Peer;
import edu.emory.viseu.overlay.model.Task;
import edu.emory.viseu.overlay.model.Workflow;
import edu.emory.viseu.overlay.util.AdaptiveScheduler;
import edu.emory.viseu.overlay.util.CloudBurstingBridge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Core engine for orchestrating decentralized workflows in Viseu.
 */
public class WorkflowEngine {
    private static final Logger logger = LogManager.getLogger(WorkflowEngine.class);

    /**
     * Executes a given workflow by dispatching ready tasks to optimal peers.
     */
    public void run(Workflow workflow) {
        logger.info("Executing Workflow: " + workflow.getName());

        while (!workflow.isFinished()) {
            List<Task> readyTasks = workflow.getReadyTasks();
            
            if (readyTasks.isEmpty() && !workflow.isFinished()) {
                logger.error("Workflow Deadlock: No tasks are ready but workflow is not finished.");
                break;
            }

            for (Task task : readyTasks) {
                executeTask(task);
            }
        }

        if (workflow.isFinished()) {
            logger.info("Workflow Successfully Completed: " + workflow.getName());
        }
    }

    /**
     * Dispatches an individual task using the AdaptiveScheduler.
     */
    private void executeTask(Task task) {
        logger.info("Dispatching " + task);
        task.setStartTime(System.currentTimeMillis());

        // 1. Find the best peer using the Adaptive Scheduler
        Optional<Peer> optimalPeer = AdaptiveScheduler.findBestPeer(task.getServiceRequired());

        if (optimalPeer.isPresent()) {
            Peer target = optimalPeer.get();
            logger.info("Selected Peer " + target.getId() + " for task " + task.getId());

            // 2. Simulate task execution on the remote node
            Object result = simulateRemoteExecution(target, task);
            
            // 3. Decentralized Arbitration (Point 4)
            if (edu.emory.viseu.overlay.util.ArbitrationService.requiresArbitration(target, task)) {
                boolean verified = edu.emory.viseu.overlay.util.ArbitrationService.verifyResult(target, task, result);
                if (!verified) {
                    logger.error("Arbitration failed for task " + task.getId() + ". Re-dispatching.");
                    executeTask(task); // Re-dispatch to find a better peer
                    return;
                }
            }

            task.setOutput("[EDGE] " + result);
            task.setEndTime(System.currentTimeMillis());
            task.setCompleted(true);
            logger.info("Task " + task.getId() + " COMPLETED on EDGE node " + target.getId());
        } else {
            // 3. Fallback to Cloud Bursting
            logger.warn("No suitable edge peer found. Falling back to CLOUD for " + task.getId());
            Object cloudResult = CloudBurstingBridge.executeOnCloud(task);
            
            task.setOutput("[CLOUD] " + cloudResult);
            task.setEndTime(System.currentTimeMillis());
            task.setCompleted(true);
            logger.info("Task " + task.getId() + " COMPLETED on CLOUD.");
        }
    }

    private Object simulateRemoteExecution(Peer peer, Task task) {
        // Simulate some processing time
        try {
            long processingTime = (long) (Math.random() * 500 + 200);
            Thread.sleep(processingTime);
        } catch (InterruptedException ignored) {}

        return "Result of " + task.getServiceRequired() + " from " + peer.getId();
    }
}
