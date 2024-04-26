package com.example.myapplication41p;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication41p.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;


public class CreateTask extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, dueDateEditText;
    private Calendar dueDate = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        Button saveButton = findViewById(R.id.saveButton);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dueDateEditText.setText(sdf.format(dueDate.getTime()));

        // Initialize


        dueDateEditText.setOnClickListener(this::showDatePickerDialog);
        saveButton.setOnClickListener(this::saveTask);
    }

    public void showDatePickerDialog(View view) {
        new DatePickerDialog(this, this::onDateSet, dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onDateSet(DatePicker view, int year, int month, int day) {
        dueDate.set(Calendar.YEAR, year);
        dueDate.set(Calendar.MONTH, month);
        dueDate.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dueDateEditText.setText(sdf.format(dueDate.getTime()));
    }


    private void saveTask(View view) {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        long dueDateMillis = dueDate.getTimeInMillis();

        if (!title.isEmpty() && !description.isEmpty()) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.addTask(title, description, dueDateMillis);
            Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity and go back
        } else {
            Toast.makeText(this, "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}