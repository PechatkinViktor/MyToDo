package com.pechatkin.sbt.mytodo.presentation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pechatkin.sbt.mytodo.R;
import com.pechatkin.sbt.mytodo.presentation.data.model.Task;
import com.pechatkin.sbt.mytodo.presentation.data.repository.TaskRepository;
import com.pechatkin.sbt.mytodo.presentation.presenter.TaskPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ITaskView {

    public static final String TASK_NOT_ADD = "Пустая задача не будет добавлена";
    public static final String OK = "OK";
    public static final String TASK_ADD = "Добавить Задачу";

    private RecyclerView mRecyclerView;
    private View mProgressFrameLayout;
    private View mFab;
    private TaskRecyclerAdapter adapter;

    private TaskPresenter mTaskPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        providePresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTaskPresenter.loadTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskPresenter.detachView();
    }

    private void providePresenter() {
        TaskRepository repository = new TaskRepository(this);
        mTaskPresenter = new TaskPresenter(this, repository);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mTaskPresenter.updateTask(String.valueOf(view.getTag()), position);
            }

            @Override
            public void onLongClick(final View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mTaskPresenter.deleteTask(view.getTag().toString(), position);
                        return true;
                    }
                });
                popupMenu.show();
            }
        }));
        mProgressFrameLayout = findViewById(R.id.progress_frame_layout);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAlertDialog();
            }
        });
    }

    private void initAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(TASK_ADD);
        final EditText input = new EditText(MainActivity.this);
        alertDialog.setView(input);
        alertDialog.setPositiveButton(OK, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().length() != 0) {
                    mTaskPresenter.addTask(String.valueOf(input.getText()));
                } else {
                    Toast.makeText(MainActivity.this, TASK_NOT_ADD, Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void showProgress() {
        mProgressFrameLayout.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mProgressFrameLayout.setVisibility(View.GONE);
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(@NonNull List<Task> taskList) {
        adapter = new TaskRecyclerAdapter(taskList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void addTask(@NonNull String taskName) {
        adapter.addTask(taskName);
        adapter.notifyItemInserted(adapter.getItemCount()-1);
    }

    @Override
    public void updateTask(@NonNull String taskName, int position) {
        adapter.updateTask(taskName);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void deleteTask(@NonNull String taskName, int position) {
        adapter.deleteTask(taskName);
        adapter.notifyItemRemoved(position);
    }
}
