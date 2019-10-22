package com.example.databasemaker.executors

import android.content.Context
import android.view.View
import com.example.databasemaker.interfaces.CreateControlView

class Dummy(override val view: View) : CreateControlView{

    override fun createView(){
    }

    override fun execute() {
    }
}