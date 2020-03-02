package ru.tpu.msk30.sudoku.game

import androidx.lifecycle.MutableLiveData
import ru.tpu.msk30.sudoku.game.Difficulty.DifficultuLevel

class SudokuGame(){

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var isTakingNotesLiveData = MutableLiveData<Boolean>()
    var highlightedKeysLiveData = MutableLiveData<Set<Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var errorCellLiveData = MutableLiveData<MutableSet<Cell>>()
    var difficultuLevelLiveData = MutableLiveData<DifficultuLevel>()
    var hintCountLiveData = MutableLiveData<Int>()
    var isPauseLiveData = MutableLiveData<Boolean>()
    var absoluteTimeLiveData = MutableLiveData<Long>()
    var liveCountLiveData = MutableLiveData<Int>()
    var endOfGameLiveData = MutableLiveData<Boolean>()

    private var selectedRow = -5
    private var selectedCol = -5
    private var errorCell: MutableSet<Cell> = mutableSetOf()
    private var isTaakingNotes = false
    private var hintCount = 3
    private var liveCount = 3
    private var isPause = false
    private var isEnd = false

    private var choosenDifficultuLevel: DifficultuLevel? = null


    private val board: Board

    init{

        val cells = List(9 * 9){ i -> Cell(i / 9, i % 9, ((i % 9) + 3 * (i / 9) + (i / 9 / 3)) % 9  + 1)}
        board = Board(9, cells)
        board.transposing()
        board.swapSmallRows()
        board.swapSmallCols()
        board.swapBigRows()
        board.swapBigCols()
        isTakingNotesLiveData.postValue(isPause)
        hintCountLiveData.postValue(hintCount)
        liveCountLiveData.postValue(liveCount)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTaakingNotes)
        endOfGameLiveData.postValue(isEnd)
        cells.forEach {cell ->
            cell.updateValue()
        }

    }

    fun isEnd(){

        val cells = cellsLiveData.value
        cells?.let{

            cells.forEach { cell ->

                if(cell.value == 0) {
                    isEnd = false
                    return
                }

            }

            isEnd = true
            endOfGameLiveData.postValue(isEnd)
        }

    }

    fun restart(){
        val cells = cellsLiveData.value
        cells?.let{

            it.forEach {cell ->

                if(!cell.isStartingCell){
                    cell.value = 0
                }

            }

            errorCell = mutableSetOf()
            absoluteTimeLiveData.postValue(0)
            errorCellLiveData.postValue(errorCell)
            cellsLiveData.postValue(cells)

        }
    }

    fun updatePause(){
        isPause = !isPause
    }

    fun handleInput(number: Int){
        if(selectedCol < 0 || selectedRow < 0) return
        val cell = board.getCell(selectedRow, selectedCol)
        if(cell.isStartingCell) return

        if(isTaakingNotes) {
            if(cell.notes.contains(number)){
                cell.notes.remove(number)
            }else{
                cell.notes.add(number)
            }
            highlightedKeysLiveData.postValue(cell.notes)
        }else {
            if (number != cell.trueValue){
                if(liveCount!= 0) {
                    cell.value = number
                    errorCell.add(cell)
                    setLiveValue()
                }
                else{
                    TODO("Сделать плохую концовку")
                }
            }else{
                cell.value = number
                if(errorCell.contains(cell)){
                    errorCell.remove(cell)
                }
                isEnd()
            }
            errorCellLiveData.postValue(errorCell)
        }
        cellsLiveData.postValue(board.cells)

    }

    fun setHintValue(){
        if(selectedCol < 0 || selectedRow < 0) return
        val cell = board.getCell(selectedRow, selectedCol)
        hintCount--
        hintCountLiveData.postValue(hintCount)
        handleInput(cell.trueValue)
    }

    fun setLiveValue(){
        liveCount--
        liveCountLiveData.postValue(liveCount)
    }

    fun updateSelectedCell(row: Int, col: Int){
        val cell = board.getCell(row, col)
        if(!cell.isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
            if (isTaakingNotes){
                highlightedKeysLiveData.postValue(cell.notes)
            }
        }
    }

    fun changeNoteTakingState(){
        if(selectedCol < 0 || selectedRow < 0) return

        if(board.getCell(selectedRow,selectedCol).value != 0) return
        isTaakingNotes = !isTaakingNotes
        isTakingNotesLiveData.postValue(isTaakingNotes)

        val curNotes = if(isTaakingNotes){
            board.getCell(selectedRow, selectedCol).notes
        }else{
            setOf<Int>()
        }

        highlightedKeysLiveData.postValue(curNotes)
    }


    fun delete(){
        if(selectedCol < 0 || selectedRow < 0) return

        val cell = board.getCell(selectedRow, selectedCol)
        if(isTaakingNotes){
            cell.notes.clear()
            highlightedKeysLiveData.postValue(setOf())
        }else{
            cell.value = 0
            if(errorCell.contains(cell)){
                errorCell.remove(cell)
            }
        }


        cellsLiveData.postValue(board.cells)
        errorCellLiveData.postValue(errorCell)
    }

    fun chooseDifficultyLevel(){
        choosenDifficultuLevel = difficultuLevelLiveData.value
        val newCells = choosenDifficultuLevel?.chooseDifficultyLevel(this.board)
        cellsLiveData.postValue(newCells)
    }


}