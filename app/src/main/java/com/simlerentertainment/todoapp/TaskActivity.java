package com.simlerentertainment.todoapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.simlerentertainment.todoapp.db.TaskContract;
import com.simlerentertainment.todoapp.db.TaskDbHelper;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    private static final String TAG = "TaskActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.task, // What view to use for the items
                    R.id.task_title, // Where to put the string of data
                    taskList); // Where to fo get the data
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        sqLiteDatabase.close();
    }

    // TODO: Change to delete by ID, not name
    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_id);
        String task = taskTextView.getText().toString();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        sqLiteDatabase.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        sqLiteDatabase.close();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String task = taskEditText.getText().toString();
                                SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                sqLiteDatabase.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        contentValues,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                sqLiteDatabase.close();

                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                alertDialog.show();
                return true;
            // TODO: Add actual logic to switch to settings
            case R.id.settings:
                Log.d(TAG, "Settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
