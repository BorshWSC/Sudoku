package ru.tpu.msk30.sudoku.game.Difficulty

import ru.tpu.msk30.sudoku.game.Board
import ru.tpu.msk30.sudoku.game.Cell
import kotlin.random.Random


interface DifficultuLevel {

    object level{
        const val LOW: Int = 0
        const val MEDIUM = 1
        const val HIGH = 2
        const val  TEST = 3
    }

    val defoultCount: Int

    fun chooseDifficultyLevel(board: Board, count: Int = defoultCount): List<Cell> {
        var cells: List<Cell> = board.cells
        var i = 0
        while (i <= count) {
            var rnd = Random.nextInt(0, cells.size)
            if (cells[rnd].value != 0) {
                cells[rnd].value = 0
                i++
            }
        }

        cells.forEach {cell ->

            if(cell.value != 0){
                cell.isStartingCell = true
            }

        }
        return cells
    }

}