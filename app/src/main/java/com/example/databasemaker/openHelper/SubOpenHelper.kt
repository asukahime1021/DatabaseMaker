package com.example.databasemaker.openHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SubOpenHelper(context : Context, dbName : String?)
        : SQLiteOpenHelper(context, dbName, null, 1){

        companion object{
            const val CREATE = "CREATE TABLE info_table (id INTEGER PRIMARY KEY AUTOINCREMENT, table_name TEXT)"
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE)
            val values = ContentValues()
            values.put("table_name","info_table")
            db?.insert("info_table", null, values)
            Log.d("INFO","create new database")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //Readable
        }
}