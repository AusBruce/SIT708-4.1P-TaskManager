package com.example.myapplication41p.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication41p.model.Task;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DUE_DATE = "dueDate";

    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_DUE_DATE + " INTEGER NOT NULL"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }


    public void updateTask(long id, String title, String description, long dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("dueDate", dueDate);

        db.update("tasks", values, "_id = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public void addTask(String title, String description, long dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("dueDate", dueDate);

        db.insert("tasks", null, values);
        db.close();
    }

    public Task getTaskById(long taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tasks", new String[] {"_id","title", "description", "dueDate"}, "_id=?", new String[]{String.valueOf(taskId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Task task = new Task(
                    cursor.getLong(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getLong(cursor.getColumnIndex("dueDate"))
            );
            cursor.close();
            return task;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    // Method to fetch all tasks from the database
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_TASKS,   // The table to query
                new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_DUE_DATE }, // The array of columns to return (pass null to get all)
                null,          // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,          // don't group the rows
                null,          // don't filter by row groups
                COLUMN_DUE_DATE + " ASC" // The sort order
        );
    }

    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "_id=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

}