package com.example.databasemaker

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.databasemaker.common.createSpinner
import com.example.databasemaker.fragments.insert.InsertFlagment
import com.example.databasemaker.openHelper.SubOpenHelper

class DataControlActivity : AppCompatActivity(),InsertFlagment.OnFragmentInteractionListener {
    val commands = listOf(
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

        createSpinner(applicationContext, findViewById(R.id.commands), commands, object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCommand = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })

        val testtables = listOf<String>("t1", "t2", "t3")
        val tableSpin = findViewById<Spinner>(R.id.tables)
        val tableAdapterSpin = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, testtables)
        tableAdapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tableSpin.adapter = tableAdapterSpin
        tableSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val frag = InsertFlagment.newInstance(dbName, testtables[position])
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
        }
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
