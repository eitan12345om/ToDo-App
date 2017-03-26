package com.simlerentertainment.todoapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.simlerentertainment.todoapp.db.TaskContract;
import com.simlerentertainment.todoapp.db.TaskDbHelper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TaskDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setupActionBar();

        mHelper = new TaskDbHelper(this);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            if (!super.onMenuItemSelected(featureId, item)) {
//                NavUtils.navigateUpFromSameTask(this);
//            }
//            return true;
//        }
//        return super.onMenuItemSelected(featureId, item);
//    }

    /**
     * Required for interface. Creates new instance of calendar
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        setDate(calendar);
    }

    /**
     * Sets date of calendar and sets field text
     */
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        ((EditText) findViewById(R.id.dueDate)).setText(dateFormat.format(calendar.getTime()));
    }

    /**
     * Creates and shows instance of datePicker on button click
     */
    public void datePicker(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "date");
    }

    public void addTaskAndLeave(View view) {
        // Get task description
        final EditText taskEditText = (EditText) findViewById(R.id.newTask);
        String task = taskEditText.getText().toString();

        // Get task date
        final EditText dateEditText = (EditText) findViewById(R.id.dueDate);
        String taskDate = dateEditText.getText().toString();
        // TODO: Use DATE information
        Toast.makeText(this, "Date: " + taskDate, Toast.LENGTH_SHORT).show();

        // Check if task was entered
        if (task.trim().length() > 0) {
            addTask(task, taskDate);
            backToMain();
        }
        else {
            Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
        }
    }

    public void addTask(String task, String date) {
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
        contentValues.put(TaskContract.TaskEntry.COL_TASK_DATE, date);
        sqLiteDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }

    public void backToMain() {
        Intent intent = new Intent(getApplicationContext(), ShowTaskActivity.class);
        startActivity(intent);
    }
}
