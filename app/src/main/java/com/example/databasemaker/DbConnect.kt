package com.example.databasemaker

import com.example.databasemaker.common.SQLCommands


interface DbConnect {
    companion object{
        var dbName : String = ""
        var tName : String = ""
        var message : String = ""

        var resultSet : MutableList<LinkedHashMap<String,Any?>> = mutableListOf()
        var columnCount = 0
        var sqlCount = 0
        var command : SQLCommands? = null
    }
}