package com.codurance.training.tasks.impl;

import com.codurance.training.tasks.CheckTaskService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class CheckTaskServiceImpl implements CheckTaskService {

    private final PrintWriter out;

    private  Map<String, List<TaskImpl>> tasks;

    public CheckTaskServiceImpl(PrintWriter writer, Map<String, List<TaskImpl>> tasks) {
        this.out = writer;
        this.tasks = tasks;

    }

    @Override
    public void check(String idString) {
        setDone(idString, true);
    }

    @Override
    public void uncheck(String idString) {
        setDone(idString, false);
    }

    @Override
    public void setDone(String idString, boolean done) {
        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            for (TaskImpl task : project.getValue()) {
                if (task.getId().equals(idString)) {
                    task.setDone(done);
                    return;
                }
            }
        }
        out.printf("Could not find a task with an ID of %s.", idString);
        out.println();
    }
}
