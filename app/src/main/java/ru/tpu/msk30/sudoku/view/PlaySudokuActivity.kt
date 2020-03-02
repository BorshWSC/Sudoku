package ru.tpu.msk30.sudoku.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import ru.tpu.msk30.sudoku.R
import ru.tpu.msk30.sudoku.game.Cell
import ru.tpu.msk30.sudoku.game.Difficulty.*
import ru.tpu.msk30.sudoku.view.custom.SudokuBoardView
import ru.tpu.msk30.sudoku.viewmodel.PlaySudokuViewModel


class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel : PlaySudokuViewModel
    private lateinit var  numerButtons : List<Button>
    private lateinit var difficultuLevel: DifficultuLevel
    private lateinit var chronometer: Chronometer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        difficultuLevel = when(intent.getIntExtra("level", 0)){
            DifficultuLevel.level.LOW ->  LowDifficulty()
            DifficultuLevel.level.MEDIUM ->  MediumDifficulty()
            DifficultuLevel.level.HIGH ->  HighDifficulty()
            DifficultuLevel.level.TEST -> Test()
            else -> LowDifficulty()
        }

        viewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this, Observer{updateNoteTakingUI(it)})
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this, Observer { updateHighlightedKeys(it) })
        viewModel.sudokuGame.errorCellLiveData.observe(this, Observer { updateErorrCell(it) })
        viewModel.sudokuGame.difficultuLevelLiveData.observe(this, Observer { chooseDifficultyLevel() })
        viewModel.sudokuGame.hintCountLiveData.observe(this, Observer { updateHintCountUI(it) })
        viewModel.sudokuGame.liveCountLiveData.observe(this, Observer { changeLiveCount(it) })
        viewModel.sudokuGame.endOfGameLiveData.observe(this, Observer { toEndOfGame() })

        viewModel.sudokuGame.difficultuLevelLiveData.postValue(difficultuLevel)


        sudokuBoardView.registerListener(this)

        chronometer = findViewById<Chronometer>(R.id.chronometer)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        chronometer.setOnChronometerTickListener {

            val elapsedMillis: Long = SystemClock.elapsedRealtime() - it.base
            var sec: Long = elapsedMillis / 1000
            var mins: Long = sec / 60
            sec -= mins * 60
            var minString: String
            var secString: String
            minString = if(mins > 0){
                if(mins > 9){
                    mins.toString()
                }else{
                    "0$mins"
                }
            }else{
                "00"
            }


            secString = if(sec > 0){
                if(sec > 9){
                    sec.toString()
                }else{
                    "0$sec"
                }
            }else{
                "00"
            }
            title = "$minString:$secString"



        }


        numerButtons = listOf(oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton,
            sevenButton, eightButton, nineButton)

        setListeners()

    }

    private fun toEndOfGame() {
        val flag = viewModel.sudokuGame.endOfGameLiveData.value
        flag?.let {
            if(it) {
                var intent: Intent = Intent(this, EndOfGameActivity::class.java)

                intent.putExtra("time", SystemClock.elapsedRealtime() - chronometer.base)
                startActivity(intent)
            }
        }

    }

    private fun changeLiveCount(it: Int) {
        textLiveNumber.text = it.toString()

    }

    private fun setListeners(){
        numerButtons.forEachIndexed{ index, button ->
            button.setOnClickListener{
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

        notesButton.setOnClickListener{
            viewModel.sudokuGame.changeNoteTakingState()
        }

        deleteButton.setOnClickListener{
            viewModel.sudokuGame.delete()
        }

        hintButton.setOnClickListener{
            val hints = viewModel.sudokuGame.hintCountLiveData.value
            if(hints != 0) {
                viewModel.sudokuGame.setHintValue()
            }
        }
    }

    private fun unsetListeners(){

        numerButtons.forEach{ button ->
            button.setOnClickListener{
            }
        }
        notesButton.setOnClickListener{
        }

        deleteButton.setOnClickListener{
        }

        hintButton.setOnClickListener{
        }

    }

    private fun updatePause() {
        viewModel.sudokuGame.updatePause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var isPause = false
        viewModel.sudokuGame.isPauseLiveData.value?.let{
            isPause = it
        }
        if(id == R.id.chronometerController){
            if(isPause){
                viewModel.sudokuGame.absoluteTimeLiveData.value?.let {
                    chronometer.base = SystemClock.elapsedRealtime() - it
                }
                chronometer.start()
                item.icon = ContextCompat.getDrawable(this, R.drawable.pause_black)
                setListeners()
            }else{
                viewModel.sudokuGame.absoluteTimeLiveData.postValue(SystemClock.elapsedRealtime() - chronometer.base)
                chronometer.stop()
                item.icon = ContextCompat.getDrawable(this, R.drawable.play_black)
                unsetListeners()
            }
            viewModel.sudokuGame.isPauseLiveData.postValue(!isPause)

        }else{

            chronometer.stop()
            viewModel.sudokuGame.restart()
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun updateHintCountUI(it: Int?) {
        textHintNumber.text = it?.toString()
    }

    private fun chooseDifficultyLevel() {
        viewModel.sudokuGame.chooseDifficultyLevel()
    }

    private fun updateErorrCell(errorCells: MutableSet<Cell>?) = errorCells?.let {
        sudokuBoardView.updateErrorCellUI(errorCells)
    }

    private fun updateHighlightedKeys(set: Set<Int>?) = set?.let{
        numerButtons.forEachIndexed{ index, button ->
            val color = if(set.contains(index + 1))  ContextCompat.getColor(this, R.color.colorPrimary) else Color.LTGRAY
            button.setBackgroundColor(color)
        }
    }

    private fun updateNoteTakingUI(isTakingNote: Boolean?) = isTakingNote?.let{
        val color = if(it) ContextCompat.getColor(this, R.color.colorPrimary) else Color.LTGRAY
            notesButton.setBackgroundColor(color)
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let{
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(it.first, it.second)
    }

    override fun onCellTouched(row: Int, col: Int){
        viewModel.sudokuGame.updateSelectedCell(row, col)

    }
}
