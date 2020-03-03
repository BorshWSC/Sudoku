package ru.tpu.msk30.sudoku.game.Difficulty

import ru.tpu.msk30.sudoku.game.Board
import ru.tpu.msk30.sudoku.game.Cell

class MediumDifficulty: DifficultuLevel {

    override val defoultCount: Int = 35
    override val currentLevel: Int
        get() = 1

    override fun chooseDifficultyLevel(board: Board, count: Int): List<Cell> {
        return super.chooseDifficultyLevel(board, defoultCount)
    }

}