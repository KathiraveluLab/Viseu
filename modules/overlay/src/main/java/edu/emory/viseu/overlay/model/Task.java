package edu.emory.viseu.overlay.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single task within a Viseu workflow.
 */
public class Task {
    private final String id;
    private final String serviceRequired;
    private final List<String> dependencies; // IDs of tasks that must complete first
    private Object input;
    private Object output;
    private boolean completed;
    
    // Performance Telemetry (Task 4)
    private long startTime;
    private long endTime;

    public Task(String id, String serviceRequired) {
        this.id = id;
        this.serviceRequired = serviceRequired;
        this.dependencies = new ArrayList<>();
        this.completed = false;
    }

    public void addDependency(String taskId) {
        dependencies.add(taskId);
    }

    public String getId() { return id; }
    public String getServiceRequired() { return serviceRequired; }
    public List<String> getDependencies() { return dependencies; }
    public Object getInput() { return input; }
    public void setInput(Object input) { this.input = input; }
    public Object getOutput() { return output; }
    public void setOutput(Object output) { this.output = output; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }
    public long getDuration() { return (endTime > startTime) ? (endTime - startTime) : 0; }

    @Override
    public String toString() {
        return String.format("Task[id=%s, service=%s, status=%s]", 
            id, serviceRequired, completed ? "COMPLETED" : "PENDING");
    }
}
