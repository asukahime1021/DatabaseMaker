package com.example.databasemaker.executors

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.databasemaker.R
import com.example.databasemaker.common.SQLCommands
import com.example.databasemaker.interfaces.CreateControlView
import kotlinx.android.synthetic.main.fragment_insert_flagment.view.*

class InputSQL(override val view: View) : CreateControlView {
    private lateinit var mContext : Context
    private val separator = System.getProperty("line.separator")
    private lateinit var editText : EditText
    private val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT
    private val matchParent = ViewGroup.LayoutParams.MATCH_PARENT

    override fun createView() {
        mContext = view.context

        val linearLayout = view.findViewById<LinearLayout>(R.id.fragLinear)

        val noticeText = TextView(mContext)
        noticeText.text = "SQLは[;改行]で区切ってください。"

        editText = EditText(mContext)
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)

        linearLayout.addView(editText, LinearLayout.LayoutParams(matchParent,wrapContent))
    }

    override fun execute() {

        // format inputted string including line separator and space
        val text : String = editText.text.toString().trim()
        val sqlMultiLine : List<String> = text.split (separator!!)
        val formattedSql = sqlMultiLine.toMutableList()
        formattedSql.removeAll { it.matches("""\s+""".toRegex()) }

        // separate sql at [;"separator"]
        val sqlLines : MutableList<MutableList<String>> = mutableListOf()
        var tmpLines : MutableList<String> = mutableListOf()
        for((index,line) in formattedSql.withIndex()){
            tmpLines.add(line)
            if(line.matches(""".*;$""".toRegex()) || index == formattedSql.size - 1) {
                val tmpList = mutableListOf<String>()
                for(content in tmpLines)
                    tmpList.add(content)
                sqlLines.add(tmpList)
                tmpLines.clear()
            }
        }
        val sqls : MutableList<String> = mutableListOf()
        for(line in sqlLines){
            var sql = ""
            for(separated in line){
                sql += "$separated "
            }
            sqls.add(sql.trim())
        }

        //execute sql
        val executor = SQLExecutor(mContext)
        for(sql in sqls){
            val sqlHead = sql.substring(0,6)
            for(command in SQLCommands.values()){
                if(sqlHead.matches(command.reg))
                    executor.executeSQL(sql, command)
            }
        }
    }
}