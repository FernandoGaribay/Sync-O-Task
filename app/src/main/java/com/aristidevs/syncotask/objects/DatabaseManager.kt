package com.aristidevs.syncotask.objects

import android.content.Context
import android.database.sqlite.SQLiteDatabase

object DatabaseManager {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var database: SQLiteDatabase

    fun initialize(context: Context) {
        dbHelper = DatabaseHelper(context)
        database = dbHelper.writableDatabase
    }

    fun getDatabase(): SQLiteDatabase {
        return database
    }
}
