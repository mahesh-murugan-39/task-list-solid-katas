package com.codurance.training.tasks.impl;

import com.codurance.training.tasks.DeleteTaskService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class DeleteTaskServiceImpl implements DeleteTaskService {

    private final PrintWriter out;

    private Map<String, List<TaskImpl>> tasks;

    public DeleteTaskServiceImpl(PrintWriter writer, Map<String, List<TaskImpl>> tasks) {
        this.out = writer;
        this.tasks = tasks;
    }

    @Override
    public void deleteTask(String idString) {
        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            project.getValue().removeIf(task -> task.getId().equals(idString));
        }
        out.printf("Could not find a task with an ID of %s.", idString);
        out.println();
    }
}
