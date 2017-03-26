package com.simlerentertainment.todoapp.db;

import android.provider.BaseColumns;

/**
 * Created by Eitan on 3/7/2017.
 */

public class TaskContract {
    public static final String DB_NAME = "com.simlerentertainment.todoapp.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE = "date";
    }
}
