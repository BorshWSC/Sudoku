package ru.tpu.msk30.sudoku.game

class Cell (
    val row: Int,
    val col: Int,
    var value: Int,
    var isStartingCell: Boolean = false,
    var notes: MutableSet<Int> = mutableSetOf(),
    var trueValue: Int = 0
){
    fun updateValue(){
        trueValue = value
    }
}