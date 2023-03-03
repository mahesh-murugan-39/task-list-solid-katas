package com.codurance.training.tasks.impl;

import com.codurance.training.tasks.ViewTaskService;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ViewTaskServiceImpl implements ViewTaskService {

    private final PrintWriter out;

    private Map<String, List<TaskImpl>> tasks;

    public ViewTaskServiceImpl(PrintWriter writer, Map<String, List<TaskImpl>> tasks) {
        this.out = writer;
        this.tasks = tasks;
    }
    @Override
    public void viewByProject() {
        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            out.println(project.getKey());
            for (TaskImpl task : project.getValue()) {
                out.printf("    [%c] %s: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    @Override
    public void viewDueTodayTasks() {

        Date today = new Date();

        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            out.println(project.getKey());
            for (TaskImpl task : project.getValue()) {
                if(task.getDeadline() != null && parseDate(task.getDeadline()).equals(parseDate(today)))
                    out.printf("    [%c] %s: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    @Override
    public void viewByDate() {

        Comparator<TaskImpl> compareByDate = Comparator.comparing(p -> parseDate(p.getCreatedDate()));


        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            out.println(project.getKey());
            List<TaskImpl> newTasks = project.getValue();
            Collections.sort(newTasks, compareByDate);
            for (TaskImpl task : newTasks) {
                out.printf("    [%c] %s: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    @Override
    public void viewByDeadline() {
        Comparator<TaskImpl> compareByDate = Comparator.comparing(p -> parseDate(p.getDeadline()));

        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            out.println(project.getKey());
            List<TaskImpl> newTasks = project.getValue();
            Collections.sort(newTasks, compareByDate);
            for (TaskImpl task : newTasks) {
                out.printf("    [%c] %s: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    private String parseDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
}
