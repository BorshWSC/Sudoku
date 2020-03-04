package ru.tpu.msk30.sudoku.save

import android.provider.BaseColumns

object ResultDataContract {
    // Table contents are grouped together in an anonymous object.
    object ResultDataEntry : BaseColumns {
        const val TABLE_NAME = "user_results"
        const val COLUMN_NAME_USER_NAME = "user_name"
        const val COLUMN_NAME_TIME = "time"
        const val COLUMN_NAME_DIFFICULTY = "difficulty"
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${ResultDataEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${ResultDataEntry.COLUMN_NAME_USER_NAME} TEXT," +
                "${ResultDataEntry.COLUMN_NAME_TIME} INTEGER," +
                "${ResultDataEntry.COLUMN_NAME_DIFFICULTY} TEXT) "

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ResultDataEntry.TABLE_NAME}"
}