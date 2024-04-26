package com.example.myapplication41p;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication41p.data.DatabaseHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditTask extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, dueDateEditText;
    private Calendar dueDate = Calendar.getInstance();

    private long taskId;  // Task ID to update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        Button saveButton = findViewById(R.id.saveButton);


        // Get task details from intent
        taskId = getIntent().getLongExtra("taskId",-1);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        long dueDateMillis = getIntent().getLongExtra("dueDate", System.currentTimeMillis());

        titleEditText.setText(title);
        descriptionEditText.setText(description);
        dueDate.setTimeInMillis(dueDateMillis);
        updateDueDateEditText();

        dueDateEditText.setOnClickListener(this::showDatePickerDialog);
        saveButton.setOnClickListener(this::updateTask);
    }

    private void updateDueDateEditText() {
        dueDateEditText.setText(String.format("%d/%d/%d",
                dueDate.get(Calendar.DAY_OF_MONTH),
                dueDate.get(Calendar.MONTH) + 1, // Month is 0-based
                dueDate.get(Calendar.YEAR)));
    }

    public void showDatePickerDialog(View view) {
        new DatePickerDialog(this, this::onDateSet, dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onDateSet(DatePicker view, int year, int month, int day) {
        dueDate.set(Calendar.YEAR, year);
        dueDate.set(Calendar.MONTH, month);
        dueDate.set(Calendar.DAY_OF_MONTH, day);
        updateDueDateEditText();
    }


private void updateTask(View view) {
    String title = titleEditText.getText().toString().trim();
    String description = descriptionEditText.getText().toString().trim();
    long dueDateMillis = dueDate.getTimeInMillis();

    if (taskId == -1) {
        Toast.makeText(this, "Error: No valid task selected!", Toast.LENGTH_LONG).show();
        finish();
        return;
    }

    if (title.isEmpty() || description.isEmpty()) {
        Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
        return;
    }

    DatabaseHelper dbHelper = new DatabaseHelper(this);
    dbHelper.updateTask(taskId, title, description, dueDateMillis);
    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();

    Intent returnIntent = new Intent();
    setResult(RESULT_OK, returnIntent); // Indicate successful update
    finish();
}





}