package ru.tpu.msk30.sudoku.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_leader_board.*
import kotlinx.android.synthetic.main.table_row.*
import ru.tpu.msk30.sudoku.R
import ru.tpu.msk30.sudoku.save.DataController
import ru.tpu.msk30.sudoku.save.ResultDataDbHelper

class LeaderBoard : AppCompatActivity() {

    private lateinit var dbHelper: ResultDataDbHelper

    private var userNames: MutableList<Any> = mutableListOf()
    private var userTimes: MutableList<Any> = mutableListOf()
    private  var difficulties: MutableList<Any> = mutableListOf()
    private var difficulty = ""

    private var currentUserNames: MutableList<Any> = mutableListOf()
    private var currentUserTimes: MutableList<Any> = mutableListOf()
    private  var currentDifficulties: MutableList<Any> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        title = "Таблица лидеров"

        dbHelper = ResultDataDbHelper(this)

        val data = DataController.readData(dbHelper)

        data["name"]?.let{
            userNames = it
        }

        data["time"]?.let {
            userTimes = it
        }

        data["difficulty"]?.let {
            difficulties = it
        }

        lowDifficultyRadioButton.isChecked = true
        onRadioButtonClick(lowDifficultyRadioButton)

    }

    private fun showLeaderBoard(){

        val size = currentUserNames.size

        addRow("№", "Имя", "Время", "Сложность")

        for(i in 0 until size){
            addRow((i + 1).toString(), currentUserNames[i] as String, currentUserTimes[i].toString(), currentDifficulties[i] as String)
        }



    }

    private fun addRow(number:String, name:String, time:String, difficulty: String){
        val inflater = LayoutInflater.from(this)
        val tableRow: TableRow = inflater.inflate(R.layout.table_row, null) as TableRow
        val serialNumberView: TextView = tableRow.findViewById(R.id.serialNumberCol)
        serialNumberView.text = number

        val nameView: TextView = tableRow.findViewById(R.id.nameCol)
        nameView.text = name

        val timeView: TextView = tableRow.findViewById(R.id.timeCol)
        timeView.text = time

        val difficultyView: TextView = tableRow.findViewById(R.id.difficultyCol)
        difficultyView.text = difficulty

        leaderBoardTable.addView(tableRow)
    }

    private fun select(){
        val size = userNames.size
        currentUserNames = mutableListOf()
        currentUserTimes = mutableListOf()
        currentDifficulties = mutableListOf()
        leaderBoardTable.removeAllViews()
        for (i in 0 until size){
            if(difficulties[i] == difficulty){
                currentUserNames.add(userNames[i])
                currentUserTimes.add(userTimes[i])
                currentDifficulties.add(difficulties[i])
            }
        }
    }

    fun onRadioButtonClick(view: View) {

        if(view is RadioButton){

            val checked = view.isChecked

            when(view.id){

                R.id.lowDifficultyRadioButton ->
                    if(checked){
                        difficulty = "Низкий"
                    }
                R.id.mediumDifficultyRadioButton ->
                    if(checked){
                        difficulty = "Средний"
                    }
                R.id.highDifficultyRadioButton ->
                    if(checked){
                        difficulty = "Сложный"
                    }
                R.id.testDifficultyRadioButton ->
                    if(checked){
                        difficulty = "Тест"
                    }

            }

            select()
            showLeaderBoard()

        }

    }
}
