package com.example.sudokuapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sudokuapp.R
import com.example.sudokuapp.game.Cell
import com.example.sudokuapp.view.custom.SudokuBoardView
import com.example.sudokuapp.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SudokuBoardView.OnTouchListner {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var attributeSet: AttributeSet


//    lateinit var sudokuBoardView : SudokuBoardView(this,attributeSet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sudokuBoardView.registerListner(this)

//        viewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSeletedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })

        val buttons = listOf(button1, button2, button3, button4, button5, button6,
            button7, button8, button9)

        buttons.forEachIndexed{index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

    }

    private fun updateCells(cells: List<Cell>?) = cells?.let{
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSeletedCellUI(cell : Pair<Int,Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(cell.first,cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}