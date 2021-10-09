package com.example.sudokuapp.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SudokuBoardView(context: Context,attributeSet: AttributeSet):View(context,attributeSet) {

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePixels = 0F

    private var selectedRow = 0
    private var selectedCol = 0

    private var listner : SudokuBoardView.OnTouchListner? = null

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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(sizePixels,sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLine(canvas)
    }

    private fun fillCells(canvas: Canvas){
        if (selectedRow == -1 || selectedCol == -1) return

        for ( r in 0..size){
            for (c in 0..size){
                if (r == selectedRow && c == selectedCol){
                    fillCell(canvas,r,c,selectedCellPaint)
                }else if (r == selectedRow || c == selectedCol){
                    fillCell(canvas,r,c,conflictingCellPaint)
                }else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize){
                    fillCell(canvas,r,c,conflictingCellPaint)
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint){
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels , (c + 1) * cellSizePixels, (r + 1) * cellSizePixels , paint)

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
        listner?.onCellTouch(possibleSelectedRow,possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int){
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun registerListner(listner: OnTouchListner){
        this.listner = listner
    }

    interface OnTouchListner{
        fun onCellTouch(row : Int, col : Int)
    }
}