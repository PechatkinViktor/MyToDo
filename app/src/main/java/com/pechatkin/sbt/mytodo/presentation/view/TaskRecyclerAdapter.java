package com.pechatkin.sbt.mytodo.presentation.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pechatkin.sbt.mytodo.R;
import com.pechatkin.sbt.mytodo.presentation.data.model.Task;

import java.util.List;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private List<Task> mTaskList;

    TaskRecyclerAdapter(@NonNull List<Task> taskList) {
        mTaskList = taskList;
    }

    void updateTask(@NonNull String taskName){
        Task task = findTask(taskName);
        if(task != null) {
            task.setTaskStatus((task.getTaskStatus() != 0) ? 0 : 1);
        }
    }

    void deleteTask(@NonNull String taskName) {
        Task task = findTask(taskName);
        if(task != null) {
            mTaskList.remove(task);
        }
    }

    private Task findTask(@NonNull String taskName) {
        for (int i = 0; i < mTaskList.size(); i++) {
            if(mTaskList.get(i).getTaskName().equals(taskName)) {
                return mTaskList.get(i);
            }
        }
        return null;
    }

    void addTask(@NonNull String taskName) {
        mTaskList.add(new Task(taskName, 0));
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindView(mTaskList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTextView;
        private CheckBox mCheckBox;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView.findViewById(R.id.task_item);
            mTextView = itemView.findViewById(R.id.task_text_view);
            mCheckBox = itemView.findViewById(R.id.task_check_box);
        }

        void bindView(@NonNull Task task) {

            mTextView.setText(task.getTaskName());
            mCheckBox.setChecked(task.getTaskStatus() != 0);
            mView.setTag(mTextView.getText());
        }
    }
}
