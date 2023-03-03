package com.codurance.training.tasks.impl;

import com.codurance.training.tasks.AddTaskService;
import com.codurance.training.tasks.CheckTaskService;
import com.codurance.training.tasks.DeleteTaskService;
import com.codurance.training.tasks.ViewTaskService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TaskList implements Runnable {
    private static final String QUIT = "quit";

    private final Map<String, List<TaskImpl>> tasks = new LinkedHashMap<>();
    private final BufferedReader in;
    private final PrintWriter out;

    private final AddTaskService addService;

    private final CheckTaskService checkService;

    private final DeleteTaskService deleteService;

    private final ViewTaskService viewService;

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        new TaskList(in, out).run();
    }

    public TaskList(BufferedReader reader, PrintWriter writer) {
        this.in = reader;
        this.out = writer;
        addService = new AddTaskServiceImpl(this.out, tasks);
        checkService = new CheckTaskServiceImpl(this.out, tasks);
        deleteService = new DeleteTaskServiceImpl(this.out, tasks);
        viewService = new ViewTaskServiceImpl(this.out, tasks);
    }

    public void run() {
        while (true) {
            out.print("> ");
            out.flush();
            String command;
            try {
                command = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (command.equals(QUIT)) {
                break;
            }
            execute(command);
        }
    }

    private void execute(String commandLine) {
        String[] commandRest = commandLine.split(" ", 2);
        String command = commandRest[0];
        switch (command) {
            case "show":
                show();
                break;
            case "add":
                addService.add(commandRest[1]);
                break;
            case "check":
                checkService.check(commandRest[1]);
                break;
            case "uncheck":
                checkService.uncheck(commandRest[1]);
                break;
            case "delete":
                deleteService.deleteTask(commandRest[1]);
                break;
            case "deadline":
                addService.addDeadlineToTask(commandRest[0], commandRest[1]);
                break;
            case "today":
                viewService.viewDueTodayTasks();
            case "view":
                String viewBy = commandRest[1];
                switch (viewBy) {
                    case "by date":
                        viewService.viewByDate();
                        break;
                    case "by deadline":
                        viewService.viewByDeadline();
                        break;
                    case "by project":
                        viewService.viewByProject();
                        break;
                }
            case "help":
                help();
                break;
            default:
                error(command);
                break;
        }
    }

    private void show() {
        for (Map.Entry<String, List<TaskImpl>> project : tasks.entrySet()) {
            out.println(project.getKey());
            for (TaskImpl task : project.getValue()) {
                out.printf("    [%c] %s: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }


    private void help() {
        out.println("Commands:");
        out.println("  show");
        out.println("  add project <project name>");
        out.println("  add task <project name> <task description>");
        out.println("  check <task ID>");
        out.println("  uncheck <task ID>");
        out.println();
    }

    private void error(String command) {
        out.printf("I don't know what the command \"%s\" is.", command);
        out.println();
    }
}
