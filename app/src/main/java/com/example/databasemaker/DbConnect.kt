package com.example.databasemaker


interface DbConnect {
    companion object{
        var dbName : String = ""
        var tName : String = ""
        var message : String = ""
    }
}