package com.example.databasemaker.executors

import android.content.Context
import android.database.SQLException
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.databasemaker.DbConnect
import com.example.databasemaker.R
import com.example.databasemaker.common.DataTypes
import com.example.databasemaker.common.DataTypes.*
import com.example.databasemaker.common.createSpinner
import com.example.databasemaker.interfaces.CreateControlView
import com.example.databasemaker.openHelper.SubOpenHelper

class CreateTable(override val view : View) : CreateControlView{

    private lateinit var mContext : Context

    private val MP = ViewGroup.LayoutParams.MATCH_PARENT
    private val WC = ViewGroup.LayoutParams.WRAP_CONTENT

    private val editList = mutableListOf<EditText>()
    val columnMap = linkedMapOf<Int, Int>()

    override fun createView(){
        mContext = view.context
        val linearLayout = view.findViewById<LinearLayout>(R.id.fragLinear)
        val typeList = listOf(
            mContext.getString(R.string.select),
            mContext.getString(R.string.integer),
            mContext.getString(R.string.text),
            mContext.getString(R.string.real))

        // input table_name
        val textTName = TextView(mContext)
        textTName.text = mContext.getString(R.string.createTName)
        val editTName = EditText(mContext)
        val editTNameTag = "editTableName"
        editTName.tag = editTNameTag
        editList.add(editTName)

        // input column_names
        var columnIndex = 0
        val textCName1 = TextView(mContext)
        textCName1.text = mContext.getString(R.string.createCName1)
        val editCName = EditText(mContext)
        val editTag = "editColumnName_$columnIndex"
        editCName.tag = editTag
        editList.add(editCName)
        val spinTag = "spinColumnName_$columnIndex"
        val spinCType = createSpinner(mContext, tag = spinTag, objects = typeList, listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setTagIndex(parent = parent, position = position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

        // add to LinearLayout
        linearLayout.addView(textTName,LinearLayout.LayoutParams(WC,WC))
        linearLayout.addView(editTName,LinearLayout.LayoutParams(MP,WC))

        linearLayout.addView(textCName1,LinearLayout.LayoutParams(WC,WC))
        linearLayout.addView(editCName,LinearLayout.LayoutParams(MP,WC))
        linearLayout.addView(spinCType,LinearLayout.LayoutParams(WC,WC))

        // add new EditText and new Spinner
        val addButton = Button(mContext)
        addButton.text = mContext.getString(R.string.add_column)
        addButton.setOnClickListener {
            columnIndex++
            val editCNameAdd = EditText(mContext)
            val editTagAdd = "editColumnName_$columnIndex"
            editCNameAdd.tag = editTagAdd
            editList.add(editCNameAdd)
            val spinTagAdd = "spinColumnName_$columnIndex"
            val spinCTypeAdd = createSpinner(mContext, tag = spinTagAdd, objects = typeList, listener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setTagIndex(parent = parent, position = position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })

            linearLayout.addView(editCNameAdd, LinearLayout.LayoutParams(MP,WC))
            linearLayout.addView(spinCTypeAdd, LinearLayout.LayoutParams(WC,WC))
        }
        val addLinear = view.findViewById<LinearLayout>(R.id.forAddButton)
        addLinear.addView(addButton, LinearLayout.LayoutParams(WC,WC))
    }

    override fun execute() {
        var query = ""
        var tableName = ""
        var columnCount = 0

        for((index, editText) in editList.withIndex()){
            when(index){
                0 -> {
                    tableName = editText.text.toString()
                    query = "create table $tableName (id INTEGER"
                }
                else -> {
                    val columnName = editText.text.toString()
                    val type = when (columnMap[index - 1]) {
                        1 -> INTEGER.name
                        2 -> TEXT.name
                        3 -> REAL.name
                        else -> ""
                    }
                    query = "$query $columnName $type"
                }
            }

            if(index == editList.size -1) query = "$query)"
            else query = "$query,"
            columnCount++
        }

        val infoQuery = "insert into info_table(table_name, column_count) values('$tableName', $columnCount)"
        SQLExecutor(mContext).create(query, infoQuery)
//        try{
//            val createHelper = SubOpenHelper(mContext, DbConnect.dbName)
//            val wdb = createHelper.writableDatabase
//            Log.d("DEBUG", query)
//            wdb.execSQL(query)
//            wdb.execSQL(infoQuery)
//            DbConnect.message = "テーブルを作成しました"
//        }catch(e : SQLException){
//            e.printStackTrace()
//            DbConnect.message = "テーブルの作成に失敗しました"
//        }
    }

    private fun setTagIndex(parent: AdapterView<*>?,position: Int){
        val tag = parent?.tag.toString()
        val tagIndex = Integer.parseInt(tag.substring(tag.indexOf("_") + 1))
        columnMap[tagIndex] = position
    }
}