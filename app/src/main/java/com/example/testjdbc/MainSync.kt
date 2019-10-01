package com.example.testjdbc

import android.app.Activity
import android.database.Cursor
import android.database.SQLException
import android.os.AsyncTask
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.testjdbc.openHelper.MainOpenHelper
import com.example.testjdbc.openHelper.SubOpenHelper

class MainSync(private val helper : MainOpenHelper, private val activity : Activity)
    : AsyncTask<String, Void, MutableList<String>>() {

    companion object{
        const val SELECT_SCHEMA = "SELECT name FROM databases"
    }

    override fun doInBackground(vararg params: String?): MutableList<String> {
        var result = mutableListOf("選択")
        findSchema(result)

        return result
    }

    override fun onPostExecute(result: MutableList<String>?) {

        val tv = activity.findViewById<TextView>(R.id.text1)
        if(result!!.size > 0) {
            // set database lists to this Activity's view
            tv.text = "データベースを選択してください。"
        }else {
            // set empty to the same
            tv.text = "データベースがありません。新しく作成してください。"
        }

    }

    private fun findSchema(result : MutableList<String>) : MutableList<String>{
        val readable = helper.readableDatabase
        try{
            val c : Cursor = readable.rawQuery(SELECT_SCHEMA, null)
            while(c.moveToNext()){
                result.add(c.getString(c.getColumnIndex("name")))
            }
            setDbList(result)
        }catch(e : SQLException){
            e.printStackTrace()
            DbConnect.message = "エラーが発生しました"
        }

        return result
    }

    private fun setDbList(result : MutableList<String>){
        val spinner = activity.findViewById<Spinner>(R.id.dbselect)
        val adapterSpin = ArrayAdapter(activity.applicationContext, android.R.layout.simple_spinner_item, result)
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpin
    }
}