package com.example.databasemaker

import org.sqlite.SQLiteDataSource

class MainTest {
    fun main(args : Array<String>){
        val sds = SQLiteDataSource()
        sds.url = "jdbc:sqlite:main/res/testdb.sqlite3"

        var result : String? = null;

        try{
            val con = sds.connection
            val st = con.createStatement()
            val rs = st.executeQuery("select * from user")

            while(rs.next()){
                val res1 = rs.getInt(1)
                val res2 = rs.getString(2)
                result = "id : ${res1} descr : ${res2}"
            }
        }catch(e : Exception){
            e.printStackTrace()
        }

        println(if(result != null) result else "")

    }
}