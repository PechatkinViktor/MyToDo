package com.pechatkin.sbt.mytodo.presentation.presenter;

import androidx.annotation.NonNull;

import com.pechatkin.sbt.mytodo.data.model.Task;
import com.pechatkin.sbt.mytodo.data.repository.TaskRepository;
import com.pechatkin.sbt.mytodo.presentation.view.ITaskView;

import java.lang.ref.WeakReference;
import java.util.List;

public class TaskPresenter {

    private final WeakReference<ITaskView> mMainActivityWeakReference;
    private final TaskRepository mTaskRepository;

    public TaskPresenter(@NonNull ITaskView mainActivity,@NonNull TaskRepository taskRepository) {
        mMainActivityWeakReference = new WeakReference<>(mainActivity);
        mTaskRepository = taskRepository;
    }

    public void deleteTask(@NonNull String taskName, int position) {
        if(mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().deleteTask(taskName, position);
        }
        mTaskRepository.deleteTask(taskName);
    }

    public void updateTask(@NonNull String newTaskName, int position) {
        if(mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().updateTask(newTaskName, position);
        }
        mTaskRepository.updateTask(newTaskName);
    }

    public void addTask(@NonNull String newTaskName) {
        mTaskRepository.insertTask(newTaskName);
        if(mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().addTask(newTaskName);
        }
    }

    public void loadTasks() {
        if(mMainActivityWeakReference.get() != null) {
            mMainActivityWeakReference.get().showProgress();
        }
        TaskRepository.OnLoadingFinishListener onLoadingFinishListener = new TaskRepository.OnLoadingFinishListener() {
            @Override
            public void onFinish(@NonNull List<Task> tasks) {
                if(mMainActivityWeakReference.get() != null) {
                    mMainActivityWeakReference.get().hideProgress();
                    mMainActivityWeakReference.get().showData(tasks);
                }
            }
        };
        mTaskRepository.loadData(onLoadingFinishListener);
    }

    public void detachView()
    {
        mMainActivityWeakReference.clear();
    }
}
