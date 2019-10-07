package com.example.databasemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.databasemaker.common.createSpinner
import com.example.databasemaker.interfaces.Syncable
import com.example.databasemaker.openHelper.MainOpenHelper
import com.example.databasemaker.openHelper.SubOpenHelper

class MainActivity : AppCompatActivity(),Syncable {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainHelper = MainOpenHelper(applicationContext)
        SpinnerSync(mainHelper, this).execute()

        val newButton = findViewById<Button>(R.id.dbcreate)
        newButton.setOnClickListener {
            buttonPush(mainHelper)
        }

        if(DbConnect.message != "")
            findViewById<TextView>(R.id.error).text = DbConnect.message
    }

    private fun buttonPush(mainHelper : MainOpenHelper){
        val dbName = findViewById<EditText>(R.id.dbtitle).text.toString()
        if(dbName == "") {
            DbConnect.message = "データベース名を入力してください"
            return
        }else {
            DbConnect.dbName = dbName
            SubOpenHelper(applicationContext, dbName)
            val wdb = mainHelper.writableDatabase
            wdb.execSQL("insert into databases (name, delete_flg) values('$dbName', 0)")
        }

        val intent = Intent(applicationContext, DataControlActivity::class.java)
        startActivity(intent)
    }

    private fun spinPush(parent : AdapterView<*>?, position : Int){
        DbConnect.dbName = parent?.getItemAtPosition(position).toString()

        val intent = Intent(applicationContext, DataControlActivity::class.java)
        startActivity(intent)
    }

    override fun onPostSync(result : MutableList<String>){
        val tv = findViewById<TextView>(R.id.text1)
        if (result!!.size > 0) {
            // set database lists to this Activity's view
            tv.text = "データベースを選択してください。"
        } else {
            // set empty to the same
            tv.text = "データベースがありません。新しく作成してください。"
        }
    }

    override fun setSpinnerList(result: MutableList<String>) {
        createSpinner(applicationContext, view = findViewById(R.id.dbselect), objects = result, listener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position > 0){
                    spinPush(parent, position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
    }
}
