package ru.tpu.msk30.sudoku.save

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ResultDataDbHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(ResultDataContract.SQL_CREATE_ENTRIES)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(ResultDataContract.SQL_DELETE_ENTRIES)
        onCreate(db)

    }

    companion object{

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "UserResult.db"

    }

}