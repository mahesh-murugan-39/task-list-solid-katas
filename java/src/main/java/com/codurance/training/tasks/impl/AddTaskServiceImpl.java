package com.codurance.training.tasks.impl;

import com.codurance.training.tasks.AddTaskService;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTaskServiceImpl implements AddTaskService {

    private final PrintWriter out;

    private Map<String, List<TaskImpl>> tasks;

    public AddTaskServiceImpl(PrintWriter writer, Map<String, List<TaskImpl>> tasks) {
        this.out = writer;
        this.tasks = tasks;
    }

    @Override
    public void add(String commandLine) {
        String[] subcommandRest = commandLine.split(" ", 2);
        String subcommand = subcommandRest[0];
        if (subcommand.equals("project")) {
            addProject(subcommandRest[1]);
        } else if (subcommand.equals("task")) {
            String[] projectTask = subcommandRest[1].split(" ", 3);
            addTask(projectTask[0], projectTask[1], projectTask[2]);
        }
    }

    @Override
    public void addProject(String name) {
        tasks.put(name, new ArrayList<TaskImpl>());
    }

    public void addTask(String taskId, String project, String description) {
        List<TaskImpl> projectTasks = tasks.get(project);
        if (projectTasks == null) {
            out.printf("Could not find a project with the name \"%s\".", project);
            out.println();
            return;
        }
        if(checkIdValidity(taskId))
            projectTasks.add(new TaskImpl(taskId, description, false));
        else
            out.println("ID should not contain spaces or special characters");
    }

    @Override
    public void addDeadlineToTask(String taskId, String deadline) {
        Date date = parseDate(deadline);
        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            for(TaskImpl task: project.getValue()) {
                if(task.getId().equals(taskId)) {
                    task.setDeadline(date);
                }
            }
        }
        out.printf("Could not find a task with an ID of %s", taskId);
        out.println();
    }

    private Date parseDate(String deadline) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(deadline);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    private boolean checkIdValidity(String id) {
        if(id.contains(" "))
            return false;

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(id);
        return !(matcher.find());
    }
}
