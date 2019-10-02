package com.example.databasemaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.databasemaker.openHelper.MainOpenHelper
import com.example.databasemaker.openHelper.SubOpenHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainHelper = MainOpenHelper(applicationContext)
        MainSync(mainHelper, this).execute()

        val spinner = findViewById<Spinner>(R.id.dbselect)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position > 0){
                    spinPush(parent, position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

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

}
