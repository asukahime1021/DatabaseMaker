package com.example.databasemaker

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.example.databasemaker.common.createSpinner
import com.example.databasemaker.fragments.insert.InsertFlagment
import com.example.databasemaker.interfaces.Syncable
import com.example.databasemaker.openHelper.SubOpenHelper

class DataControlActivity : AppCompatActivity(),InsertFlagment.OnFragmentInteractionListener,Syncable {
    val commands = listOf(
        "作成",
        "検索",
        "挿入",
        "更新"
    )
    var fragmentOn = false
    var selectedCommand : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_control)

        val dbName = DbConnect.dbName
        val helper = SubOpenHelper(applicationContext, dbName)
        SpinnerSync(helper, this).execute()

        /*
        TODO:
        テーブル作成についてはテーブル名を選ばせてはいけない
        作成は「作成」選択時に作成画面を出し、他のコマンドはテーブル選択で画面表示にする。
         */
        createSpinner(applicationContext, findViewById(R.id.commands), commands, object : AdapterView.OnItemSelectedListener{
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
        createSpinner(applicationContext, findViewById(R.id.tables), result, object : AdapterView.OnItemSelectedListener{
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

    override fun onFragmentInteraction(uri: Uri) {

    }
}
