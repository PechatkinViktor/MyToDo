package com.pechatkin.sbt.mytodo.presentation.data.model;

import androidx.annotation.NonNull;

public class Task {

    private final String mTaskName;
    private int mTaskStatus;

    public Task(@NonNull String taskName, int taskStatus) {
        mTaskName = taskName;
        mTaskStatus = taskStatus;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public int getTaskStatus() {
        return mTaskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        mTaskStatus = taskStatus;
    }
}
