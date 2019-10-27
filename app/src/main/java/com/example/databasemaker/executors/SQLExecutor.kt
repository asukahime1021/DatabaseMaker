package com.example.databasemaker.executors

import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.core.database.getBlobOrNull
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.example.databasemaker.DbConnect
import com.example.databasemaker.common.SQLCommands
import com.example.databasemaker.openHelper.SubOpenHelper
import java.sql.SQLException

class SQLExecutor(mContext : Context) {
    private val helper = SubOpenHelper(mContext, DbConnect.dbName)

    fun executeSQL(query : String, command : SQLCommands){
        when(command.name){
            "SELECT" -> select(query)
            "CREATE" -> create(query)
        }

        DbConnect.sqlCount++
    }

    fun insert(query : String){
        try {
            val db = helper.writableDatabase
            db.execSQL(query)
            DbConnect.message = "SUCCESS!!"
        }catch (e : SQLException){
            e.printStackTrace()
            DbConnect.message = "failed..."
        }
    }

    fun select(query : String){
        try{
            val db = helper.readableDatabase
            val c = db.rawQuery(query, null)

            val columnTypeMap = linkedMapOf<String, Int>()

            val valueMap = linkedMapOf<String, Any?>()

            while(c.moveToNext()){
                if(c.isLast) {
                    for (i in 1..c.columnCount) {
                        columnTypeMap[c.getColumnName(i - 1)] = c.getType(i - 1)
                    }
                    columnTypeMap.forEach { (columnName, type) -> valueMap[columnName] = columnName }
                    DbConnect.resultSet.add(valueMap)
                }
                columnTypeMap.forEach { (columnName,type) ->
                    valueMap[columnName] = when(type){
                        Cursor.FIELD_TYPE_BLOB -> c.getBlobOrNull(c.getColumnIndex(columnName))
                        Cursor.FIELD_TYPE_FLOAT -> c.getFloatOrNull(c.getColumnIndex(columnName))
                        Cursor.FIELD_TYPE_INTEGER -> c.getIntOrNull(c.getColumnIndex(columnName))
                        Cursor.FIELD_TYPE_STRING -> c.getStringOrNull(c.getColumnIndex(columnName))
                        else -> null
                    }
                }
                DbConnect.resultSet.add(valueMap)
            }
            DbConnect.command = SQLCommands.SELECT
            DbConnect.message = "success!"
            DbConnect.columnCount = c.columnCount

            c.close()
        }catch (e : SQLException){
            e.printStackTrace()
            DbConnect.message = "failed..."
        }
    }

    fun create(query : String, infoQuery : String = ""){
        try{
            val wdb = helper.writableDatabase
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