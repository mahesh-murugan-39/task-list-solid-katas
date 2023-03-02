package com.codurance.training.tasks;

public interface CheckTaskService {
    public void check(String idString);

    public void uncheck(String idString);

    public void setDone(String idString, boolean done);
}
