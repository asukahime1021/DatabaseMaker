package com.example.databasemaker.interfaces

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.databasemaker.R
import kotlinx.android.synthetic.main.fragment_insert_flagment.view.*

interface CreateControlView {

    val view : View

    /**
     * Create Views to display on "InsertFragment"
     * Override this and add Views to LinearLayout in "fragment_insert_fragment.xml"
     * This LinearLayout has "orientation='vertical'"
     *
     * notice : When overrides this, you needs to call "super()"
     */
    fun createView()

    /**
     * Execute a process on "execute" Button clicked
     * each classes implement this and do process to control database
     */
    fun execute()

}