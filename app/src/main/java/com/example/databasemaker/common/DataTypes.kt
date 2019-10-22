package com.example.databasemaker.common

enum class DataTypes {
    INTEGER{
        override fun validate(value : String) : Boolean{
            var result = false
            if(value.matches("\\d+".toRegex())) result = true

            return result
        }

        override fun format(value : String): Any = value.toInt()
    },

    TEXT{
        override fun validate(value : String) : Boolean = true

        override fun format(value: String): Any = value
    },

    REAL{
        override fun validate(value : String) : Boolean{
            var result = false
            if(value.matches("\\d+\\.\\d+".toRegex())) result = true

            return result
        }

        override fun format(value: String): Any = value.toBigDecimal()
    };

    abstract fun validate(value : String) : Boolean
    abstract fun format(value : String) : Any
}