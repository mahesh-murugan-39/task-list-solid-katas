package com.codurance.training.tasks.impl;

import com.codurance.training.tasks.Task;

import java.util.Date;
import java.util.Optional;

public final class TaskImpl implements Task {
    private final String id;
    private final String description;
    private boolean done;
    private Date deadline;

    private final Date createdDate;

    public TaskImpl(String id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.deadline = null;
        this.createdDate = new Date();
    }

    public TaskImpl(String id, String description, boolean done, Date deadline) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
        this.createdDate = new Date();
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", done=" + done +
                ", deadline=" + deadline +
                '}';
    }
}
