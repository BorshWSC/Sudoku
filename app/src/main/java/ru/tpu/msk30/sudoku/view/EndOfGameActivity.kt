package ru.tpu.msk30.sudoku.view

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_end_of_game.*
import ru.tpu.msk30.sudoku.R
import ru.tpu.msk30.sudoku.save.ResultDataContract
import ru.tpu.msk30.sudoku.save.ResultDataDbHelper

class EndOfGameActivity : AppCompatActivity() {

    private lateinit var dbHelper: ResultDataDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_of_game)
        dbHelper = ResultDataDbHelper(this)

        acceptButton.setOnClickListener {

            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {

                put(ResultDataContract.ResultDataEntry.COLUMN_NAME_USER_NAME, plain_text_input.text.toString())
                put(ResultDataContract.ResultDataEntry.COLUMN_NAME_TIME, intent.getLongExtra("time", 0))
                put(ResultDataContract.ResultDataEntry.COLUMN_NAME_DIFFICULTY, intent.getStringExtra("difficulty"))

            }

            val newRowId = db?.insert(ResultDataContract.ResultDataEntry.TABLE_NAME, null, values)

            val newIntent = Intent(this, ResultActivity::class.java)
            newIntent.putExtra("difficulty", intent.getStringExtra("difficulty")?.toString())
            startActivity(newIntent)
        }
    }
}
