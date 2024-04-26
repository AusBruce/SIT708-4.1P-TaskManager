package com.example.myapplication41p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication41p.data.DatabaseHelper;
import com.example.myapplication41p.model.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskDetail extends AppCompatActivity {

    private TextView taskTitleTextView, taskDescriptionTextView, taskDueDateTextView;

    private long taskId;
    private long dueDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskTitleTextView = findViewById(R.id.taskTitleTextView);
        taskDescriptionTextView = findViewById(R.id.taskDescriptionTextView);
        taskDueDateTextView = findViewById(R.id.taskDueDateTextView);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button editButton = findViewById(R.id.editButton);
        Button backButton = findViewById(R.id.backButton);


        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear all other activities and start a new task
            startActivity(intent);
            finish(); // Optional if you do not want this activity in the back stack
        });


        // Initialize


        taskId = getIntent().getLongExtra("taskId", -1);
        if (taskId != -1) {
            loadTaskDetails(taskId);

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(TaskDetail.this, EditTask.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("title", taskTitleTextView.getText().toString());
                intent.putExtra("description", taskDescriptionTextView.getText().toString());
                intent.putExtra("dueDate", dueDateMillis);
                startActivityForResult(intent, 100); // 100 is the request code for identification
            });
            deleteButton.setOnClickListener(v -> deleteTask(taskId));
        } else {
            Toast.makeText(this, "Error: Task ID is missing.", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (taskId != -1) {
            loadTaskDetails(taskId);  // Reload task details every time this Activity resumes
        }
    }

    private void loadTaskDetails(long taskId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Task task = dbHelper.getTaskById(taskId);
        if (task != null) {
            taskTitleTextView.setText(task.getTitle());
            taskDescriptionTextView.setText(task.getDescription());
            dueDateMillis = task.getDueDate();
            String formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(dueDateMillis));
            taskDueDateTextView.setText(formattedDate);
        } else {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // The task was edited, reload the task details
            loadTaskDetails(taskId);
        }
    }

    private void deleteTask(long taskId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.deleteTask(taskId);
        Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
        finish();  // Exit the activity after deletion
    }



}

