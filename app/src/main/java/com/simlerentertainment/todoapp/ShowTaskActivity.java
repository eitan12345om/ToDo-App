package com.simlerentertainment.todoapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.simlerentertainment.todoapp.db.TaskContract;
import com.simlerentertainment.todoapp.db.TaskDbHelper;

import java.util.ArrayList;

import static com.simlerentertainment.todoapp.R.layout.task;

// TODO: Add Date to database and task.xml

public class ShowTaskActivity extends AppCompatActivity {

    private final String TAG = "ShowTaskActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

//        mTaskListView.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(view.getContext(), "You Clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });

        // On click allows user to update activity
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "You clicked me!");
                Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                intent.putExtra("Update", true);
                // TODO: Change this to ID of task
                intent.putExtra("Description", ((TextView) view.findViewById(R.id.task_title)).getText().toString());
                startActivity(intent);
            }
        });

        // Long click listener for copying task information
        mTaskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                // Copies text to clipboard
                String text = ((TextView) view.findViewById(R.id.task_title)).getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create intent for CreateTaskActivity
                Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                intent.putExtra("Update", false);
                startActivity(intent);
            }
        });

        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TaskContract.TaskEntry.TABLE,  // Name of the table to be queried
                new String[]{  // Which columns are returned
                        TaskContract.TaskEntry._ID,
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COL_TASK_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    task, // What view to use for the items
                    R.id.task_title, // Where to put the string of data
                    taskList); // Where to get the data
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
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = taskTextView.getText().toString();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        sqLiteDatabase.delete(
                TaskContract.TaskEntry.TABLE,  // Where to delete
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",  // Boolean check
                new String[]{task});  // What to delete
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
            case R.id.settings:
//                TODO: Change Settings Menu

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
