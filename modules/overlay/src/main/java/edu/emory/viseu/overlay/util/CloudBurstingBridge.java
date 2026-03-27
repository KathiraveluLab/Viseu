package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.model.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Fallback bridge to offload tasks to the public cloud (simulated).
 */
public class CloudBurstingBridge {
    private static final Logger logger = LogManager.getLogger(CloudBurstingBridge.class);
    
    // Simulated Cloud API Endpoint
    private static final String CLOUD_API_URL = "https://api.viseu-cloud.io/v1/execute";

    /**
     * Forwards a task to the public cloud when edge resources are unavailable.
     * 
     * @param task The task to be executed.
     * @return The result from the cloud service.
     */
    public static Object executeOnCloud(Task task) {
        logger.warn("CLOUD BURSTING TRIGGERED for task: " + task.getId());
        logger.info("Forwarding request to cloud endpoint: " + CLOUD_API_URL);

        // Simulate network latency to cloud (usually higher than local edge)
        try {
            long cloudLatency = (long) (Math.random() * 800 + 400); // 400ms - 1200ms
            Thread.sleep(cloudLatency);
        } catch (InterruptedException ignored) {}

        String result = "CLOUD_RESULT::" + task.getServiceRequired() + "::SUCCESS";
        logger.info("Received result from Cloud for task " + task.getId());
        
        return result;
    }
}
