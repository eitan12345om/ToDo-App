package com.simlerentertainment.todoapp;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.simlerentertainment.todoapp.db.TaskContract;
import com.simlerentertainment.todoapp.db.TaskDbHelper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerFragment.TimePickerFiller {

    // Instance Variables
    private TaskDbHelper mHelper;
    private EditText editTextDescription, editTextDate, editTextTime;
    private ImageButton deleteDateButton, deleteTimeButton;
    private String ID;
    private boolean updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        setupActionBar();

        Intent intent = getIntent();

        mHelper = new TaskDbHelper(this);
        editTextDescription = (EditText) findViewById(R.id.taskDescription);
        editTextDate = (EditText) findViewById(R.id.due_date);
        editTextTime = (EditText) findViewById(R.id.due_time);
        deleteDateButton = (ImageButton) findViewById(R.id.delete_date);
        deleteTimeButton = (ImageButton) findViewById(R.id.delete_time);

        Bundle extras = intent.getExtras();
        updateTask = extras.getBoolean("Update");
        // Check if updating. If so, update interface to reflect update state
        if (updateTask) {
            Task task = extras.getParcelable("Task");

            // Update input fields
            editTextDescription.setText(task.getDescription());
            editTextDescription.setSelection(task.getDescription().length());
            editTextDate.setText(task.getDate());
            editTextTime.setText(task.getTime());

            if (!TextUtils.isEmpty(editTextDate.getText())) {
                deleteDateButton.setVisibility(View.VISIBLE);
                editTextTime.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(editTextTime.getText())) {
                deleteTimeButton.setVisibility(View.VISIBLE);
            }

            ID = String.valueOf(task.getID());
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

    /**
     * Required for interface. Creates new instance of calendar
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month, day);
        setDate(calendar);
        deleteDateButton.setVisibility(View.VISIBLE);
    }

    /**
     * Sets date of calendar and sets field text
     */
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        ((EditText) findViewById(R.id.due_date)).setText(dateFormat.format(calendar.getTime()));
        editTextTime.setVisibility(View.VISIBLE);
    }

    /**
     * Creates and shows instance of datePicker on button click
     */
    public void datePicker(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "date");
    }

    public void deleteDate(View view) {
        editTextDate.setText("");
        deleteDateButton.setVisibility(View.GONE);
        deleteTime(view);
        editTextTime.setVisibility(View.GONE);
    }

    public void timePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void deleteTime(View view) {
        editTextTime.setText("");
        deleteTimeButton.setVisibility(View.GONE);
    }

    public void addTaskAndLeave(View view) {
        String description = editTextDescription.getText().toString().trim();
        String taskDate = editTextDate.getText().toString();
        String taskTime = editTextTime.getText().toString();

        // Check if task was entered
        if (description.length() > 0) {
            prepareTask(description, taskDate, taskTime);
            backToMain();
        } else {
            Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareTask(String description, String date, String time) {
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COL_TASK_TITLE, description);
        contentValues.put(TaskContract.TaskEntry.COL_TASK_DATE, date);
        contentValues.put(TaskContract.TaskEntry.COL_TASK_TIME, time);
        if (!updateTask) {
            mHelper.createToDo(sqLiteDatabase, contentValues);
        } else {
            mHelper.updateToDo(sqLiteDatabase, contentValues, ID);
        }
        sqLiteDatabase.close();
    }

    public void backToMain() {
        Intent intent = new Intent(getApplicationContext(), ShowTaskActivity.class);
        // To stop ability to go back to this activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void fillInTime(int hourOfDay, int minute) {
        editTextTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
        deleteTimeButton.setVisibility(View.VISIBLE);
    }

//    private String formatTime(int hourOfDay, int minute) {
//        if (hourOfDay > 11) {
//            hourOfDay = hourOfDay - 12
//        }
//    }
}