package com.codurance.training.tasks;

import com.codurance.training.tasks.impl.TaskImpl;

import java.util.*;

public interface ViewTaskService {

    public void viewByProject();

    public void viewDueTodayTasks();

    public void viewByDate();

    public void viewByDeadline();

}
