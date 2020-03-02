package ru.tpu.msk30.sudoku.game.Difficulty

import ru.tpu.msk30.sudoku.game.Board
import ru.tpu.msk30.sudoku.game.Cell

class LowDifficulty: DifficultuLevel {

    override val defoultCount: Int = 28

    override fun chooseDifficultyLevel(board: Board, count: Int): List<Cell> {

        return super.chooseDifficultyLevel(board, defoultCount)

    }

}