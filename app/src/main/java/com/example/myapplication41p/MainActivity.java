package com.example.myapplication41p;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication41p.data.DatabaseHelper;
import com.example.myapplication41p.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView tasksRecyclerView;
    private FloatingActionButton addTaskButton;
    private TaskAdapter taskAdapter;
    private ArrayList<Task> taskList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        loadTasks();
        taskAdapter = new TaskAdapter(taskList, this);
        tasksRecyclerView.setAdapter(taskAdapter);

        addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateTask.class);
            startActivity(intent);
        });
    }

//    private void loadTasks() {
//        taskList.clear(); // Clear the existing tasks to prevent duplicates
//
//        Cursor cursor = dbHelper.getAllTasks();
//        if (cursor != null) {
//            int titleIndex = cursor.getColumnIndex("title");
//            int descriptionIndex = cursor.getColumnIndex("description");
//            int dueDateIndex = cursor.getColumnIndex("dueDate");
//            int idIndex = cursor.getColumnIndex("_id");
//
//            if (titleIndex == -1 || descriptionIndex == -1 || dueDateIndex == -1 || idIndex == -1) {
//                Log.e("MainActivity", "One or more columns not found.");
//                Toast.makeText(this, "Database error: Column not found.", Toast.LENGTH_LONG).show();
//            } else {
//                while (cursor.moveToNext()) {
//                    long id = cursor.getLong(idIndex);
//                    String title = cursor.getString(titleIndex);
//                    String description = cursor.getString(descriptionIndex);
//                    long dueDate = cursor.getLong(dueDateIndex);
//
//                    Task task = new Task(id, title, description, dueDate);
//                    taskList.add(task);
//                }
//            }
//            cursor.close();
//        } else {
//            Toast.makeText(this, "Error loading tasks.", Toast.LENGTH_SHORT).show();
//        }
//
//        taskAdapter.notifyDataSetChanged(); // Notify the adapter after updating the list
//    }

    private void loadTasks() {

        taskList.clear(); // Clear the existing tasks to prevent duplicates
        Cursor cursor = dbHelper.getAllTasks();
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");
            int dueDateIndex = cursor.getColumnIndex("dueDate");

            if (titleIndex == -1 || descriptionIndex == -1 || dueDateIndex == -1) {
                // Handle the error, perhaps log it or show a user message
                Log.e("MainActivity", "Column not found.");
            } else {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    long dueDate = cursor.getLong(dueDateIndex);

                    Task task = new Task(id, title, description, dueDate);
                    taskList.add(task);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadTasks(); // Refresh the list in case any data has changed
        taskAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Refresh the task list
            loadTasks();
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(this, TaskDetail.class);
        intent.putExtra("taskId", task.getId());
        startActivityForResult(intent, 100); // 100 is the request code for identification
    }


}
