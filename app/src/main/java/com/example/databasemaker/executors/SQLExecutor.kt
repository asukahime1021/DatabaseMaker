package com.example.databasemaker.executors

import android.content.Context
import android.util.Log
import com.example.databasemaker.DbConnect
import com.example.databasemaker.common.SQLCommands
import com.example.databasemaker.openHelper.SubOpenHelper
import java.sql.SQLException

class SQLExecutor(val mContext : Context) {

    fun executeSQL(query : String, command : SQLCommands){
        when(command.name){
            "SELECT" -> select(query)
            "CREATE" -> create(query)
        }
    }

    fun insert(query : String){
        try {
            val insertHelper = SubOpenHelper(mContext, DbConnect.dbName)
            val db = insertHelper.writableDatabase
            db.execSQL(query)
            DbConnect.message = "SUCCESS!!"
        }catch (e : SQLException){
            e.printStackTrace()
            DbConnect.message = "failed..."
        }
    }

    fun select(query : String){

    }

    fun create(query : String, infoQuery : String = ""){
        try{
            val createHelper = SubOpenHelper(mContext, DbConnect.dbName)
            val wdb = createHelper.writableDatabase
            wdb.execSQL(query)
            if(infoQuery != "")
                wdb.execSQL(infoQuery)
            DbConnect.message = "テーブルを作成しました"
        }catch(e : SQLException){
            e.printStackTrace()
            DbConnect.message = "テーブルの作成に失敗しました"
        }
    }

    fun update(query : String){

    }
}