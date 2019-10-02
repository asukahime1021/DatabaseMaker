package com.example.databasemaker.openHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MainOpenHelper(private val context : Context)
    : SQLiteOpenHelper(context, DB_NAME, null, 1){

    companion object{
        val CREATE = "CREATE TABLE databases (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, delete_flg INTEGER)"
        val DB_NAME = "database"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE)
        Log.d("MainOpenHelper","create databases")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //Readable
    }
}