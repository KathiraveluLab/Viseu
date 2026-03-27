package edu.emory.viseu.overlay.util;

import edu.emory.viseu.overlay.WorkflowEngine;
import edu.emory.viseu.overlay.model.Task;
import edu.emory.viseu.overlay.model.Workflow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Automated Benchmarking Suite for Viseu.
 * Evaluates performance across different workflow scenarios.
 */
public class ViseuBenchmark {
    private static final Logger logger = LogManager.getLogger(ViseuBenchmark.class);

    public void runBenchmark(int iterations) {
        logger.info("--- STARTING VISEU PERFORMANCE BENCHMARK ---");
        logger.info("Iterations: " + iterations);

        List<Long> latencies = new ArrayList<>();
        int edgeCount = 0;
        int cloudCount = 0;

        for (int i = 0; i < iterations; i++) {
            logger.info("Iteration " + (i + 1) + "/" + iterations);
            Workflow wf = createTestWorkflow("Iteration-" + i);
            
            long startTime = System.currentTimeMillis();
            new WorkflowEngine().run(wf);
            long endTime = System.currentTimeMillis();
            
            latencies.add(endTime - startTime);

            for (Task t : wf.getTasks()) {
                if (t.getOutput() != null) {
                    if (t.getOutput().toString().contains("[EDGE]")) edgeCount++;
                    if (t.getOutput().toString().contains("[CLOUD]")) cloudCount++;
                }
            }
        }

        printReport(latencies, edgeCount, cloudCount);
    }

    private Workflow createTestWorkflow(String name) {
        Workflow wf = new Workflow(name);
        
        Task t1 = new Task("T1", "DicomPreprocessing");
        Task t2 = new Task("T2", "ImageNetClassification");
        t2.addDependency("T1");
        Task t3 = new Task("T3", "ResultStorage");
        t3.addDependency("T2");

        wf.addTask(t1);
        wf.addTask(t2);
        wf.addTask(t3);
        
        return wf;
    }

    private void printReport(List<Long> latencies, int edgeTasks, int cloudTasks) {
        long totalLatency = 0;
        long min = Long.MAX_VALUE;
        long max = 0;

        for (long l : latencies) {
            totalLatency += l;
            if (l < min) min = l;
            if (l > max) max = l;
        }

        double avgLatency = (double) totalLatency / latencies.size();
        double cloudRatio = (double) cloudTasks / (edgeTasks + cloudTasks) * 100;

        logger.info("--- VISEU BENCHMARK REPORT ---");
        logger.info(String.format("Average E2E Latency: %.2f ms", avgLatency));
        logger.info(String.format("Latency Range: [%d ms - %d ms]", min, max));
        logger.info(String.format("Edge Tasks: %d", edgeTasks));
        logger.info(String.format("Cloud Tasks: %d", cloudTasks));
        logger.info(String.format("Cloud Offloading Ratio: %.1f%%", cloudRatio));
        logger.info("-------------------------------");
    }
}
