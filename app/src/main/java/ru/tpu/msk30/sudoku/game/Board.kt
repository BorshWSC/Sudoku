package ru.tpu.msk30.sudoku.game

import kotlin.random.Random

class Board(val size: Int, val cells: List<Cell>){


    fun getCell(row: Int, col: Int) = cells[row*size + col]




    fun transposing(){
        cells.forEach {cell ->

            val row : Int = cell.row
            val col : Int = cell.col

            if(col < row) {

                var temp: Int = this.getCell(row, col).value
                this.getCell(row, col).value = this.getCell(col, row).value
                this.getCell(col, row).value = temp

            }
        }
    }

    fun swapSmallRows(){

        for(rndArea in 0..2) {
            var rndRawFirst: Int = 0
            var rndRawSecond: Int = 0

            while (rndRawSecond == rndRawFirst) {

                rndRawFirst = Random.nextInt(0 + rndArea * 3, 3 + rndArea * 3)
                rndRawSecond = Random.nextInt(0 + rndArea * 3, 3 + rndArea * 3)

            }


            val cell: Cell = this.getCell(rndRawFirst, 0)
            for (j in cells.indexOf(cell)..(cells.indexOf(cell) + 8)) {
                val firstCell: Cell = cells[j]
                val secondCell: Cell = this.getCell(rndRawSecond, firstCell.col)
                var temp: Int = firstCell.value
                firstCell.value = secondCell.value
                secondCell.value = temp

            }
        }

    }

    fun swapSmallCols(){

        this.transposing()
        this.swapSmallRows()
        this.transposing()

    }

    fun swapBigRows(){

        var rndAreaFirst: Int = Random.nextInt(0, 3)
        var rndAreaSecond: Int = Random.nextInt(0, 3)

        while(rndAreaFirst == rndAreaSecond){
            rndAreaSecond = Random.nextInt(0, 3)
        }

        if(rndAreaFirst > rndAreaSecond){
            rndAreaFirst = rndAreaSecond.also { rndAreaSecond = rndAreaFirst }
        }

        for (currentRow in 0..2){
            val cell: Cell = this.getCell(rndAreaFirst * 3 + currentRow, 0)
            for (j in cells.indexOf(cell)..(cells.indexOf(cell) + 8)) {
                val firstCell: Cell = cells[j]
                val secondCell: Cell = cells[j + kotlin.math.abs(rndAreaFirst - rndAreaSecond) * 27]
                var temp: Int = firstCell.value
                firstCell.value = secondCell.value
                secondCell.value = temp

            }
        }

    }

    fun swapBigCols(){
        this.transposing()
        this.swapBigRows()
        this.transposing()
    }

}