package com.simlerentertainment.todoapp.db;

import android.provider.BaseColumns;

/**
 * @author Eitan created on 3/7/2017.
 */

public class TaskContract {
    static final String DB_NAME = "com.simlerentertainment.todoapp.db";
    static final int DB_VERSION = 2;

    public class TaskEntry implements BaseColumns {
        static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE = "date";
        public static final String COL_TASK_TIME = "time";
    }
}
