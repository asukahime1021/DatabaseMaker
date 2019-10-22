package com.example.databasemaker

import android.app.Activity
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import com.example.databasemaker.interfaces.Syncable
import com.example.databasemaker.openHelper.MainOpenHelper

class SpinnerSync(private val helper : SQLiteOpenHelper, private val activity : Activity)
    : AsyncTask<String, Void, MutableList<String>>() {

    companion object{
        const val SELECT_SCHEMA = "SELECT name FROM databases"
        const val SELECT_TABLE = "SELECT table_name FROM info_table"
    }

    override fun doInBackground(vararg params: String?): MutableList<String> {
        var result = mutableListOf("選択")
        findDescr(result)

        return result
    }

    override fun onPostExecute(result: MutableList<String>?) {
        if(activity is Syncable) activity.onPostSync(result!!)
    }

    private fun findDescr(result : MutableList<String>) : MutableList<String>{
        val readable = helper.readableDatabase
        if(activity is Syncable) {
            val query = when {
                activity is MainActivity -> SELECT_SCHEMA
                activity is DataControlActivity -> SELECT_TABLE
                else -> ""
            }
            try {
                val c: Cursor = readable.rawQuery(query, null)
                while (c.moveToNext()) {
                    result.add(c.getString(0))
                }
                c.close()

                activity.setSpinnerList(result)
            } catch (e: SQLException) {
                e.printStackTrace()
                DbConnect.message = "エラーが発生しました"
            }
        }
        return result
    }

}