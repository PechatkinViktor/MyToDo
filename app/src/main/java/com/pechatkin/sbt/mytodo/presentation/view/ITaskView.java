package com.pechatkin.sbt.mytodo.presentation.view;

import androidx.annotation.NonNull;

import com.pechatkin.sbt.mytodo.presentation.data.model.Task;

import java.util.List;

public interface ITaskView {

    void showProgress();
    void hideProgress();
    void showData(@NonNull List<Task> taskList);
    void addTask(@NonNull String taskName);
    void updateTask(@NonNull String taskName, int position);
    void deleteTask(@NonNull String taskName, int position);
}
