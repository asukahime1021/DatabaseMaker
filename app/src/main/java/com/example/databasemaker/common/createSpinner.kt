package com.example.databasemaker.common

import android.R
import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

fun createSpinner(context : Context, view : Spinner, objects : List<*>, listener : AdapterView.OnItemSelectedListener) : Spinner{
    val adapterSpin = ArrayAdapter(context, R.layout.simple_spinner_item, objects)
    adapterSpin.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    view.adapter = adapterSpin
    view.onItemSelectedListener = listener

    return view
}