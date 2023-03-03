package com.codurance.training.tasks;

public interface AddTaskService {
    public void add(String commandLine);

    public void addProject(String name);

    public void addTask(String id, String project, String description);

    public void addDeadlineToTask(String taskId, String deadline);
}
