package ru.tpu.msk30.sudoku.view

import android.content.RestrictionEntry
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*
import ru.tpu.msk30.sudoku.R
import ru.tpu.msk30.sudoku.save.ResultDataContract
import ru.tpu.msk30.sudoku.save.ResultDataDbHelper

class ResultActivity : AppCompatActivity() {

    private lateinit var dbHelper: ResultDataDbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        dbHelper = ResultDataDbHelper(this)
        readData()
    }


    private fun readData(){
        TODO("Переписать передачу данных")
        val db = dbHelper.readableDatabase
        val projection = arrayOf(ResultDataContract.ResultDataEntry.COLUMN_NAME_USER_NAME, ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME, ResultDataContract.ResultDataEntry.COLUMN_NAME_DIFFICULTY)
        val selection = "${ResultDataContract.ResultDataEntry.COLUMN_NAME_DIFFICULTY} = ?"
        val selectionArgs = arrayOf(intent.getStringExtra("difficulty"))
        val sortOrder = "${ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME} DESC"
        val cursor = db.query(
            ResultDataContract.ResultDataEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        showData(cursor)
    }

    private fun showData(cursor: Cursor){

        val userTimes = mutableListOf<Long>()
        with(cursor){

            while(moveToNext()){
                val time = getLong(getColumnIndexOrThrow(ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME))
                userTimes.add(time)
            }

        }

        textView.text = userTimes[0].toString()

    }
}
