package com.example.databasemaker.fragments.insert

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.databasemaker.DbConnect

import com.example.databasemaker.R
import com.example.databasemaker.executors.*
import com.example.databasemaker.interfaces.CreateControlView
import com.example.databasemaker.openHelper.SubOpenHelper

private const val ARG_PARAM1 = "dbName"
private const val ARG_PARAM2 = "tName"
private const val ARG_PARAM3 = "position"

class InsertFragment : Fragment() {

    private var dbName: String? = null
    private var tName: String? = null
    private var position: Int = -1
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            dbName = it.getString(ARG_PARAM1)
            tName = it.getString(ARG_PARAM2)
            position = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_flagment, container, false)
    }

    fun backToActivity(message : String) {
        listener?.onFragmentInteraction(message)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * Processing branches by input value
     * "create" : 1 "search" : 2 "insert" : 3 "update(delete)" : 4
     * @param view fragment view
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val command : CreateControlView = when(position){
            1 -> CreateTable(view)
            2 -> SelectTable(view, tName)
            3 -> InsertRecord(view, tName)
            5 -> InputSQL(view)
            else -> Dummy(view)
        }

        command.createView()

        val execButton = view.findViewById<Button>(R.id.execute)
        execButton.setOnClickListener {
            view.findViewById<TextView>(R.id.insert).text = "pressed"
            command.execute()
            backToActivity(DbConnect.message)
        }

        view.findViewById<TextView>(R.id.insert).text = "$dbName : $tName"
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(message : String)
    }

    /**
     * To initialize this fragment, Use this object
     *
     */
    companion object {

        @JvmStatic
        fun newInstance(dbName: String, tName : String, position : Int) =
            InsertFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, dbName)
                    putString(ARG_PARAM2, tName)
                    putInt(ARG_PARAM3, position)
                }
            }
    }
}
