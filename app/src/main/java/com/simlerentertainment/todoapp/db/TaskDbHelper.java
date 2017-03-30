package com.simlerentertainment.todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void updateToDo(String oldDescription, SQLiteDatabase sqLiteDatabase,
                           ContentValues contentValues) {
        sqLiteDatabase.updateWithOnConflict(
                TaskContract.TaskEntry.TABLE,
                contentValues,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", // TODO: Update to use ID
                new String[]{oldDescription}, // TODO: Update to use ID
                SQLiteDatabase.CONFLICT_REPLACE);
    }
}
