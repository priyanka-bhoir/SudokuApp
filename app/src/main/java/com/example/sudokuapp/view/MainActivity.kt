package com.example.sudokuapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sudokuapp.R
import com.example.sudokuapp.view.custom.SudokuBoardView
import com.example.sudokuapp.viewmodel.PlaySudokuViewModel

class MainActivity : AppCompatActivity(), SudokuBoardView.OnTouchListner {

    private lateinit var viewModel: PlaySudokuViewModel
    lateinit var sudokuBoardView : SudokuBoardView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sudokuBoardView.registerListner(this)

        viewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSeletedCellUI(it) })
    }

    private fun updateSeletedCellUI(cell : Pair<Int,Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(cell.first,cell.second)
    }



    override fun onCellTouch(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}