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

import com.example.databasemaker.R
import com.example.databasemaker.executors.CreateTable
import com.example.databasemaker.executors.Dummy
import com.example.databasemaker.interfaces.CreateFragmentView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "tName"
private const val ARG_PARAM3 = "position"

class InsertFlagment : Fragment() {

    private var param1: String? = null
    private var tName: String? = null
    private var position: Int = -1
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
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

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val command : CreateFragmentView = when(position){
            0 -> CreateTable(view)
//            1 -> false
//            2 -> false
            else -> Dummy(view)
        }

        command.createView()

        val execButton = view.findViewById<Button>(R.id.execute)
        execButton.setOnClickListener {
            view.findViewById<TextView>(R.id.insert).text = "pressed"
        }

        view.findViewById<TextView>(R.id.insert).text = "$param1 : $tName"

    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, tName : String, position : Int) =
            InsertFlagment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, tName)
                    putInt(ARG_PARAM3, position)
                }
            }
    }
}
