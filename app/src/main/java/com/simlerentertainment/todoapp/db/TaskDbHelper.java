package com.simlerentertainment.todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.simlerentertainment.todoapp.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eitan on 3/7/2017.
 */

public class TaskDbHelper extends SQLiteOpenHelper {
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COL_TASK_DATE + " DATE);";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(sqLiteDatabase);
    }

    public void createToDo(SQLiteDatabase sqLiteDatabase, ContentValues contentValues) {
        sqLiteDatabase.insertWithOnConflict(
                TaskContract.TaskEntry.TABLE,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateToDo(SQLiteDatabase sqLiteDatabase, ContentValues contentValues,
                           String ID) {
        sqLiteDatabase.updateWithOnConflict(
                TaskContract.TaskEntry.TABLE,
                contentValues,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{ID},
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteToDo(SQLiteDatabase sqLiteDatabase, String ID) {
        sqLiteDatabase.delete(
                TaskContract.TaskEntry.TABLE,  // Where to delete
                TaskContract.TaskEntry._ID + " = ?",  // Boolean check
                new String[]{ID}); // What to delete
    }

    /**
     * @return ArrayList of tasks in task list
     */
    public ArrayList<Task> getToDoList() {
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        // Query the database
        Cursor cursor = sqLiteDatabase.query(
                TaskContract.TaskEntry.TABLE,  // Name of the table to be queried
                new String[]{  // Which columns are returned
                        TaskContract.TaskEntry._ID,
                        TaskContract.TaskEntry.COL_TASK_TITLE,
                        TaskContract.TaskEntry.COL_TASK_DATE},
                null, null, null, null, null);

        // Iterate the results
        while (cursor.moveToNext()) {
            int ID = Integer.valueOf(cursor.getString(0));
            String description = cursor.getString(1);
            String date = cursor.getString(2);
            taskList.add(new Task(ID, description, date));
        }

        cursor.close();
        sqLiteDatabase.close();

        return taskList;
    }
}
