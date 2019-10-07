package com.example.databasemaker

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.databasemaker.common.createSpinner
import com.example.databasemaker.fragments.insert.InsertFlagment
import com.example.databasemaker.interfaces.Syncable
import com.example.databasemaker.openHelper.SubOpenHelper
import kotlinx.android.synthetic.main.activity_data_control.*

class DataControlActivity : AppCompatActivity(),InsertFlagment.OnFragmentInteractionListener,Syncable {

    private val WC = ViewGroup.LayoutParams.WRAP_CONTENT
    var fragmentOn = false
    var selectedCommand : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_control)

        if(intent.getIntExtra("restart", 0) != 1)
            DbConnect.message = ""

        val dbName = DbConnect.dbName
        val helper = SubOpenHelper(applicationContext, dbName)
        SpinnerSync(helper, this).execute()
        val commands = listOf(
            getString(R.string.select),
            getString(R.string.create),
            getString(R.string.search),
            getString(R.string.insert),
            getString(R.string.update)
        )

        val messageView = findViewById<TextView>(R.id.message)
        messageView.text = DbConnect.message

        /*
        TODO:
        テーブル作成についてはテーブル名を選ばせてはいけない
        作成は「作成」選択時に作成画面を出し、他のコマンドはテーブル選択で画面表示にする。
         */
        // set command_list
        createSpinner(applicationContext, view = findViewById(R.id.commands), objects = commands, listener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCommand = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
    }

    /**
     * set table_list
     */
    override fun setSpinnerList(result: MutableList<String>) {
        if(result.isEmpty()) result.add("nothing")
        createSpinner(applicationContext, view = findViewById(R.id.tables), objects = result, listener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val frag = InsertFlagment.newInstance(DbConnect.dbName, result[position], selectedCommand)
                val tran = supportFragmentManager.beginTransaction()
                if(!fragmentOn)
                    tran.add(R.id.frag, frag)
                else
                    tran.replace(R.id.frag, frag)
                fragmentOn = true
                tran.commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
    }

    override fun onPostSync(result: MutableList<String>) {

    }

    override fun onFragmentInteraction(message : String) {
        finish()
        intent.putExtra("restart", 1)
        startActivity(intent)
    }
}
