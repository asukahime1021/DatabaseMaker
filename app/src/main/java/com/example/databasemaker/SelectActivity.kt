package com.example.databasemaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView

class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val dataList = mutableListOf<String>()
        for((i, dataMap) in DbConnect.resultSet.withIndex()){
            if(i > 0)
                dataMap.forEach { (column, value) -> dataList.add(column) }
            else {
                dataMap.forEach { (column, value) ->
                    if(value == null)
                        dataList.add("(null)")
                    else
                        dataList.add(value.toString())
                }
            }

        }

        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, dataList)
        val gridView = findViewById<GridView>(R.id.selectGrid)
        gridView.numColumns = DbConnect.columnCount
        gridView.adapter = adapter
    }
}
