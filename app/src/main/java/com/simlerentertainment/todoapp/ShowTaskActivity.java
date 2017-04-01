package com.simlerentertainment.todoapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.simlerentertainment.todoapp.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.List;

import static com.simlerentertainment.todoapp.R.layout.task;

// TODO: Add Date to database and task.xml

public class ShowTaskActivity extends AppCompatActivity {

    private final String TAG = "ShowTaskActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<Task> mAdapter;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        updateUI();

        // On click allows user to update activity
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                intent.putExtra("Update", true);
                intent.putExtra("Task", mAdapter.getItem(i));
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
    }

    private void updateUI() {
        ArrayList<Task> taskList = mHelper.getToDoList();

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
    }

    public void deleteTask(View view) {
        // Delete from database using ID
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        mHelper.deleteToDo(sqLiteDatabase, getIDFromView((View) view.getParent()));
        sqLiteDatabase.close();

        updateUI();
    }

    private String getIDFromView(View view) {
        int position = mTaskListView.getPositionForView(view);
        Task task = mAdapter.getItem(position);
        return String.valueOf(task.getID());
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


    private class CustomAdapter<T> extends ArrayAdapter<T> {
        // Constructors
        public CustomAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return null; // Change
        }
    }
}
