package com.example.databasemaker.common

enum class SQLCommands {
    SELECT{
        override val reg = """select""".toRegex()
    },
    INSERT{
        override val reg = """insert""".toRegex()
    },
    CREATE{
        override val reg = """create""".toRegex()
    },
    UPDATE{
        override val reg = """update""".toRegex()
    },
    ALTER{
        override val reg = """alter.*""".toRegex()
    },
    PRAGMA{
        override val reg = """pragma""".toRegex()
    },
    DELETE{
        override val reg = """delete""".toRegex()
    },
    DROP{
        override val reg = """drop.*""".toRegex()
    },
    TRUNCATE{
        override val reg = """trunca""".toRegex()
    };

    abstract val reg : Regex

}