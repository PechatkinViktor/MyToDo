package com.pechatkin.sbt.mytodo.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.pechatkin.sbt.mytodo.data.model.Task;
import com.pechatkin.sbt.mytodo.data.source.TaskDbHelper;
import com.pechatkin.sbt.mytodo.data.source.TaskDbOperation;

import java.util.List;

public class TaskRepository {

    private final Context mContext;

    public TaskRepository(@NonNull Context context) {
        mContext = context;
    }

    public void deleteTask(@NonNull String taskName) {
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask(TaskDbOperation.DELETE);
        loadDataAsyncTask.execute(taskName);
    }
    public void updateTask(@NonNull String taskName) {
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask(TaskDbOperation.UPDATE);
        loadDataAsyncTask.execute(taskName);
    }

    public void insertTask(@NonNull String taskName) {
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask(TaskDbOperation.INSERT);
        loadDataAsyncTask.execute(taskName);
    }

    public void loadData(@NonNull OnLoadingFinishListener onLoadingFinishListener) {
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask(onLoadingFinishListener, TaskDbOperation.SELECT_ALL);
        loadDataAsyncTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadDataAsyncTask extends AsyncTask<String, Void, List<Task>> {


        private final OnLoadingFinishListener mOnLoadingFinishListener;
        private final TaskDbOperation mOperation;

        LoadDataAsyncTask(@NonNull OnLoadingFinishListener onLoadingFinishListener,
                          @NonNull TaskDbOperation taskDbOperation) {
            mOnLoadingFinishListener = onLoadingFinishListener;
            mOperation = taskDbOperation;
        }
        LoadDataAsyncTask(@NonNull TaskDbOperation taskDbOperation) {
            mOnLoadingFinishListener= null;
            mOperation = taskDbOperation;
        }

        @Override
        protected List<Task> doInBackground(String... strings) {

            switch (mOperation) {
                case INSERT:
                    TaskDbHelper.insertTask(mContext, strings[0]);
                    break;
                case SELECT_ALL:
                    return TaskDbHelper.selectAllTasks(mContext);
                case DELETE:
                    TaskDbHelper.deleteTask(mContext, strings[0]);
                    break;
                case UPDATE:
                    TaskDbHelper.updateTaskStatus(mContext, strings[0]);
                    break;
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Task> taskList) {
            super.onPostExecute(taskList);
            if (mOperation == TaskDbOperation.SELECT_ALL) {
                if (mOnLoadingFinishListener != null) {
                    mOnLoadingFinishListener.onFinish(taskList);
                }
            }
        }
    }

    public interface OnLoadingFinishListener {
        void onFinish(List<Task> packageModels);
    }
}
