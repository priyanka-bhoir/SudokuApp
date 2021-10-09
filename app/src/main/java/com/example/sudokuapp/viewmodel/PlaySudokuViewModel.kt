package com.example.sudokuapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sudokuapp.game.SudokuGame
import com.example.sudokuapp.view.custom.SudokuBoardView

class PlaySudokuViewModel : ViewModel(){
    val sudokuGame = SudokuGame()
}