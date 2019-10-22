package com.example.databasemaker.interfaces

interface Syncable {
    fun setSpinnerList(result : MutableList<String>)

    fun onPostSync(result : MutableList<String>)
}