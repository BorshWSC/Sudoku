package ru.tpu.msk30.sudoku.game.Difficulty

import ru.tpu.msk30.sudoku.game.Board
import ru.tpu.msk30.sudoku.game.Cell

class HighDifficulty: DifficultuLevel {

    override val defoultCount: Int = 42
    override val currentLevel: Int
        get() = 2

    override fun chooseDifficultyLevel(board: Board,count: Int): List<Cell> {
        return super.chooseDifficultyLevel(board, defoultCount)

    }

}