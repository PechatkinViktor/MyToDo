package com.pechatkin.sbt.mytodo.presentation.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.pechatkin.sbt.mytodo.presentation.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tasks.db";
    private static final int CHECK_BOX_DEFAULT_VALUE = 0;

    private TaskDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TaskDbSchema.TaskTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TaskDbSchema.TaskTable.Cols.TASK_NAME   + " text, " +
                TaskDbSchema.TaskTable.Cols.TASK_STATUS + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void deleteTask(Context context, String name) {

        SQLiteDatabase sqLiteDatabase = new TaskDbHelper(context).getWritableDatabase();
        String selection = TaskDbSchema.TaskTable.Cols.TASK_NAME + " = ?";
        String[] selectionArgs = { name };
        sqLiteDatabase.delete(TaskDbSchema.TaskTable.NAME, selection, selectionArgs);
    }

    public static void updateTaskStatus(Context context, String name) {

        SQLiteDatabase sqLiteDatabase = new TaskDbHelper(context).getWritableDatabase();
        String selection = TaskDbSchema.TaskTable.Cols.TASK_NAME + " = ?";
        String[] selectionArgs = { name };
        int oldStatus = findStatus(sqLiteDatabase, selection, selectionArgs);
        int newStatus = (oldStatus != 0) ? 0 : 1;
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.TaskTable.Cols.TASK_STATUS, newStatus);
        sqLiteDatabase.update(TaskDbSchema.TaskTable.NAME, values, selection, selectionArgs);

    }

    private static int findStatus(SQLiteDatabase sqLiteDatabase, String selection, String[] selectionArgs) {
        String[] projection = {TaskDbSchema.TaskTable.Cols.TASK_STATUS};
        int oldStatus = 1;
        try (Cursor cursor = sqLiteDatabase.query(TaskDbSchema.TaskTable.NAME, projection, selection, selectionArgs, null, null, null)) {
            while (cursor.moveToNext()) {
                oldStatus = cursor.getInt(cursor.getColumnIndex(TaskDbSchema.TaskTable.Cols.TASK_STATUS));
            }
        }
        return oldStatus;
    }

    public static void insertTask(Context context, String name) {
        SQLiteDatabase sqLiteDatabase = new TaskDbHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.TaskTable.Cols.TASK_NAME, name);
        values.put(TaskDbSchema.TaskTable.Cols.TASK_STATUS, CHECK_BOX_DEFAULT_VALUE);
        sqLiteDatabase.insert(TaskDbSchema.TaskTable.NAME, null, values);
    }

    public static List<Task> selectAllTasks(Context context) {

        SQLiteDatabase sqLiteDatabase = new TaskDbHelper(context).getReadableDatabase();
        String[] projection = {
                TaskDbSchema.TaskTable.Cols.TASK_NAME,
                TaskDbSchema.TaskTable.Cols.TASK_STATUS
        };
        List<Task> taskList = new ArrayList<>();
        try (Cursor cursor = sqLiteDatabase.query(TaskDbSchema.TaskTable.NAME, projection, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(TaskDbSchema.TaskTable.Cols.TASK_NAME));
                int status = cursor.getInt(cursor.getColumnIndex(TaskDbSchema.TaskTable.Cols.TASK_STATUS));
                taskList.add(new Task(name, status));
            }
        }
        return taskList;
    }
}
