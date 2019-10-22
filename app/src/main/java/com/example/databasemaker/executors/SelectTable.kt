package com.example.databasemaker.executors

import android.content.Context
import android.view.View
import com.example.databasemaker.DbConnect
import com.example.databasemaker.common.DataTypes
import com.example.databasemaker.interfaces.CreateControlView
import com.example.databasemaker.openHelper.SubOpenHelper

class SelectTable(override val view: View, private val tName : String?) : CreateControlView {
    private lateinit var mContext : Context
    private val columns = mutableListOf<String>()
    private val types = mutableListOf<DataTypes>()

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


    }

    override fun execute() {

    }
}