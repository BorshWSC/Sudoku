package ru.tpu.msk30.sudoku.view.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import ru.tpu.msk30.sudoku.R
import ru.tpu.msk30.sudoku.game.Cell

class SudokuBoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet){

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePixels = 0F
    private var noteSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private var errorCell: MutableSet<Cell> = mutableSetOf()

    private var listener: SudokuBoardView.OnTouchListener? = null

    private var cells: List<Cell>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F

    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F

    }

    private val selectedCellPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#008577")
    }

    private val rightCellPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    private val conflictCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }

    private val errorCellPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.RED
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }
    private val startingCellPaint = Paint().apply{
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#acacac")
    }

    private val noteTestPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas){
        updateMeasurements(width)
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun updateMeasurements(width: Int) {
        cellSizePixels = (width / size).toFloat()

        noteSizePixels = cellSizePixels / sqrtSize.toFloat()
        noteTestPaint.textSize = noteSizePixels

        textPaint.textSize = cellSizePixels / 1.5F
        startingCellPaint.textSize = cellSizePixels / 1.2F
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach{cell ->

            val value = cell.value
            val textBounds = Rect()


            if(value == 0){
                cell.notes.forEach{note ->
                    val valueString = note.toString()
                    val rowInCell = (note - 1) / sqrtSize
                    val colInCell = (note - 1) % sqrtSize
                    noteTestPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
                    val textWidth = noteTestPaint.measureText(valueString)
                    val textHight = textBounds.height()

                    canvas.drawText(
                        valueString,
                        (cell.col * cellSizePixels) + (colInCell * noteSizePixels) + noteSizePixels / 2F - textWidth / 2F,
                        (cell.row * cellSizePixels) + (rowInCell * noteSizePixels) + noteSizePixels / 2F + textHight / 2F,
                        noteTestPaint

                    )

                }
            }else {
                val valueString = cell.value.toString()
                val row = cell.row
                val col = cell.col
                val paintToUse = if (cell.isStartingCell) startingCellTextPaint else textPaint
                paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWith = textPaint.measureText(valueString)
                val textHight = textBounds.height()
                canvas.drawText(
                    valueString, (col * cellSizePixels) + cellSizePixels / 2 - textWith,
                    (row * cellSizePixels) + cellSizePixels / 2 + textHight, textPaint
                )
            }
        }
    }

    private fun fillCells(canvas: Canvas) {
        //if(selectedRow == -1 || selectedCol == -1) return

        cells?.forEach{
            val row = it.row
            val col = it.col
            if(it.isStartingCell){
                fillCell(canvas, row, col, startingCellPaint)

            }else if (errorCell.contains(it)){

                fillCell(canvas, row, col, errorCellPaint)

            }else if (row == selectedRow && col == selectedCol){
                if(it.value != 0){
                    fillCell(canvas, row, col, rightCellPaint)
                }else {
                    fillCell(canvas, row, col, selectedCellPaint)
                }

            }
            else if(row == selectedRow || col == selectedCol){
                fillCell(canvas, row, col, conflictCellPaint)
            }
            else if(row / sqrtSize == selectedRow  / sqrtSize && col / sqrtSize == selectedCol / sqrtSize){
                fillCell(canvas, row, col, conflictCellPaint)

            }

        }
    }

    private fun fillCell(canvas: Canvas, row: Int, col: Int, selectedCellPaint: Paint) {
        canvas.drawRect(col * cellSizePixels, row * cellSizePixels, (col +1) * cellSizePixels, (row +1) * cellSizePixels, selectedCellPaint)
    }

    private fun drawLines(canvas: Canvas){
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            val paintToUse = when (i % sqrtSize) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                paintToUse
            )

            canvas.drawLine(
                0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                paintToUse
            )


        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        return when(event.action){
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }

    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int){
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun updateErrorCellUI(errorCells: MutableSet<Cell>){
        errorCell = errorCells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener){
        this.listener = listener
    }

    fun updateCells(cells: List<Cell>){
        this.cells = cells
        invalidate()
    }

    interface OnTouchListener{
        fun onCellTouched(row: Int, col: Int)
    }

}