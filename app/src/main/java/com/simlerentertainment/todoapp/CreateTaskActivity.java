package com.simlerentertainment.todoapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.simlerentertainment.todoapp.db.TaskContract;
import com.simlerentertainment.todoapp.db.TaskDbHelper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Instance Variables
    private TaskDbHelper mHelper;
    private EditText editTextDescription;
    private EditText editTextDate;
    private boolean updateTask;
    private String oldDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setupActionBar();

        Intent intent = getIntent();

        mHelper = new TaskDbHelper(this);
        editTextDescription = (EditText) findViewById(R.id.newTask);
        editTextDate = (EditText) findViewById(R.id.dueDate);


        Bundle extras = intent.getExtras();
        updateTask = extras.getBoolean("Update");
        // Check if updating. If so, update text field
        // TODO: ADD Date change, too
        if (updateTask) {
            editTextDescription.setText(extras.getString("Description"));
            editTextDescription.setSelection(editTextDescription.getText().length());
            oldDescription = extras.getString("Description");
            Button button = (Button) findViewById(R.id.submit_task);
            button.setText(R.string.update_task);
            setTitle(R.string.update_task_activity);
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.on_back_button_title);
        builder.setMessage(R.string.on_back_button_message);
        builder.setPositiveButton(R.string.back_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.back_button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
        String task = editTextDescription.getText().toString().trim();

        // Get task date
        String taskDate = editTextDate.getText().toString();

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
        if (!updateTask) {
            sqLiteDatabase.insertWithOnConflict(
                    TaskContract.TaskEntry.TABLE,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
        else {
            sqLiteDatabase.updateWithOnConflict(
                    TaskContract.TaskEntry.TABLE,
                    contentValues,
                    TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", // TODO: Update to use ID
                    new String[]{oldDescription}, // TODO: Update to use ID
                    SQLiteDatabase.CONFLICT_REPLACE
            );
        }
        sqLiteDatabase.close();
    }

    public void backToMain() {
        Intent intent = new Intent(getApplicationContext(), ShowTaskActivity.class);
        // To stop ability to go back to this activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
