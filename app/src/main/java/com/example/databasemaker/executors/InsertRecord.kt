package com.example.databasemaker.executors

import android.content.Context
import android.provider.ContactsContract
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.databasemaker.DbConnect
import com.example.databasemaker.R
import com.example.databasemaker.common.DataTypes
import com.example.databasemaker.interfaces.CreateControlView
import com.example.databasemaker.openHelper.SubOpenHelper
import java.sql.SQLException

class InsertRecord(override val view: View, private val tName : String?) : CreateControlView{
    private lateinit var mContext : Context
    private val columns = mutableListOf<String>()
    private val types = mutableListOf<DataTypes>()
    private val editList = mutableListOf<EditText>()
    private val typeMap = linkedMapOf(
        "INTEGER" to "整数",
        "TEXT" to "文字",
        "REAL" to "小数"
    )

    private val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

    override fun createView() {
        mContext = view.context

        val rdb = SubOpenHelper(mContext, DbConnect.dbName).readableDatabase
        val c = rdb.rawQuery  ("PRAGMA table_info('$tName')", null)
        while(c.moveToNext()){
            columns.add(c.getString(c.getColumnIndex("name")))
            types.add(when(c.getString(c.getColumnIndex("type"))){
                "INTEGER" -> DataTypes.INTEGER
                "TEXT" -> DataTypes.TEXT
                else -> DataTypes.REAL
            })
        }
        c.close()
//        DbConnect.message = columns.toString()

        val linearLayout = view.findViewById<LinearLayout>(R.id.fragLinear)
        for((index,column) in columns.withIndex()){
            val text = TextView(mContext)
            text.text = "$column : ${typeMap[types[index].name]}"
            val edit = EditText(mContext)
            editList.add(edit)
            edit.inputType = when(types[index]){
                DataTypes.INTEGER -> InputType.TYPE_CLASS_NUMBER
                DataTypes.REAL -> InputType.TYPE_NUMBER_FLAG_DECIMAL
                else -> InputType.TYPE_CLASS_TEXT
            }

            linearLayout.addView(text, LinearLayout.LayoutParams(wrapContent,wrapContent))
            linearLayout.addView(edit, LinearLayout.LayoutParams(wrapContent,wrapContent))
        }

    }

    override fun execute() {
        val insertColumn = mutableListOf<String>()
        val insertData = mutableListOf<String>()
        for((index,edit) in editList.withIndex()){
            if(types[index].validate(edit.text.toString())) {
                insertColumn.add(columns[index])
                insertData.add("'${edit.text}'")
            }else{
                DbConnect.message = "\"${columns[index]}\" failed"
                return
            }
        }
        var queryColumn = "INSERT INTO $tName("
        var queryData = ") values ("
        for((index,column) in insertColumn.withIndex()){
            if(index != insertColumn.size-1) {
                queryColumn += "$column, "
                queryData += "${insertData[index]}, "
            }else {
                queryColumn += column
                queryData += "${insertData[index]})"
            }
        }

        val query = "$queryColumn$queryData"
        SQLExecutor(mContext).insert(query)
//        val insertHelper = SubOpenHelper(mContext,DbConnect.dbName)
//        val db = insertHelper.writableDatabase
//        db.execSQL("$queryColumn$queryData")
//        DbConnect.message = "SUCCESS!!"
    }
}