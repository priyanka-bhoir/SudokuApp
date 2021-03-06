package com.example.sudokuapp.view.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.sudokuapp.game.Cell

class SudokuBoardView(context: Context,attributeSet: AttributeSet):View(context,attributeSet) {

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePixels = 0F

    private var selectedRow = 0
    private var selectedCol = 0

    private var listner : SudokuBoardView.OnTouchListner? = null

    private var cells:List<Cell>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE;
        color = Color.BLACK;
        strokeWidth = 4F;
    }
    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE;
        color = Color.BLACK;
        strokeWidth = 2F;
    }
    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE;
        color = Color.parseColor("#6ead3a")
    }
    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE;
        color = Color.parseColor("#efedef")
    }
    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 24F
    }
    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 32F
        typeface = Typeface.DEFAULT_BOLD
    }
    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#acacac")
        textSize = 32F
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(sizePixels,sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLine(canvas)
        drawText(canvas)
    }

    private fun fillCells(canvas: Canvas){
        if (selectedRow == -1 || selectedCol == -1) return

//        for ( r in 0..size){
//            for (c in 0..size){
//
//            }
//        }
        cells?.forEach{
            val r = it.row
            val c = it.col

            if (it.isStartingCell){
                fillCell(canvas,r,c,startingCellPaint)
            }else if (r == selectedRow && c == selectedCol){
                fillCell(canvas,r,c,selectedCellPaint)
            }else if (r == selectedRow || c == selectedCol){
                fillCell(canvas,r,c,conflictingCellPaint)
            }else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize){
                fillCell(canvas,r,c,conflictingCellPaint)
            }
        }
    }

    fun updateCells(cells : List<Cell>){
        this.cells = cells
        invalidate()
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint){
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels , (c + 1) * cellSizePixels, (r + 1) * cellSizePixels , paint)

    }

    private fun drawText(canvas: Canvas){
        cells?.forEach{

            val row = it.row
            val col = it.col

            val valueString = it.value.toString()


            val paintToUse = if (it.isStartingCell) startingCellPaint else textPaint
            val textBounds = Rect()
            paintToUse.getTextBounds(valueString,1,valueString.length,textBounds)
            val textWidth = paintToUse.measureText(valueString)
            val textHeight = textBounds.height()

            canvas.drawText(valueString,
                (col*cellSizePixels)+ cellSizePixels / 2 - textWidth / 2,
                (row * cellSizePixels) + cellSizePixels / 2 - textHeight / 2,
                paintToUse)

        }
    }

    private fun drawLine(canvas: Canvas) {
        // this if of side box
        canvas.drawRect(0F,0F,width.toFloat(),height.toFloat(),thickLinePaint)

        // this is for
        for (i in 1 until size){
            val paintToUse = when (i% sqrtSize){
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            // this is for drawing cross lines
            canvas.drawLine(i*cellSizePixels,0F,i*cellSizePixels,height.toFloat(),paintToUse)

            canvas.drawLine(0F,i*cellSizePixels,width.toFloat(),i*cellSizePixels,paintToUse)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        return when (event.action){
            MotionEvent.ACTION_DOWN -> {
                hadleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }

    }

    private fun hadleTouchEvent(x: Float, y: Float){
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listner?.onCellTouched(possibleSelectedRow,possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int){
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun registerListner(listner: SudokuBoardView.OnTouchListner){
        this.listner = listner
    }

    interface OnTouchListner{
        fun onCellTouched(row : Int, col : Int)
    }
}