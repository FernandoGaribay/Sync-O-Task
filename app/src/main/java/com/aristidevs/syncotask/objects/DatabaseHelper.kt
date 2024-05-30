package com.aristidevs.syncotask.objects

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Sync-O-Task-mobile.db"
        const val TABLE_TASKS = "Tasks"

        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_START_TIME = "startTime"
        const val COLUMN_END_TIME = "endTime"
        const val COLUMN_PRIORITY = "priority"
        const val COLUMN_STATE = "state"
        const val COLUMN_SUB_TASKS = "subTasks"
        const val COLUMN_TAGS = "tags"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_TASKS (
                $COLUMN_TITLE TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_START_TIME TEXT,
                $COLUMN_END_TIME TEXT,
                $COLUMN_PRIORITY TEXT,
                $COLUMN_STATE INTEGER,
                $COLUMN_SUB_TASKS TEXT,
                $COLUMN_TAGS TEXT
            )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }
}
