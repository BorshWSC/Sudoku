package ru.tpu.msk30.sudoku.viewmodel

import androidx.lifecycle.ViewModel
import ru.tpu.msk30.sudoku.game.Difficulty.DifficultuLevel
import ru.tpu.msk30.sudoku.game.Difficulty.LowDifficulty
import ru.tpu.msk30.sudoku.game.SudokuGame

class PlaySudokuViewModel : ViewModel(){

    val sudokuGame = SudokuGame()

}