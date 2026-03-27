package edu.emory.viseu.overlay.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Directed Acyclic Graph (DAG) of Tasks to be executed across the Viseu network.
 */
public class Workflow {
    private final String name;
    private final List<Task> tasks;

    public Workflow(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public String getName() {
        return name;
    }

    public Optional<Task> getTaskById(String id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    /**
     * Identifies tasks that are ready for execution (all dependencies completed).
     */
    public List<Task> getReadyTasks() {
        List<Task> ready = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                boolean allDependenciesMet = true;
                for (String depId : task.getDependencies()) {
                    Optional<Task> dep = getTaskById(depId);
                    if (dep.isEmpty() || !dep.get().isCompleted()) {
                        allDependenciesMet = false;
                        break;
                    }
                }
                if (allDependenciesMet) {
                    ready.add(task);
                }
            }
        }
        return ready;
    }

    public boolean isFinished() {
        return tasks.stream().allMatch(Task::isCompleted);
    }
}
