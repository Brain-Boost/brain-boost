package com.example.brainboost

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlin.random.Random

class TicTacToeViewModel: ViewModel() {
    private val _state = mutableStateOf(TicTicToeState())
    val state: State<TicTicToeState> = _state

    fun setButton(id: Int) {
        if(_state.value.victor == null && _state.value.buttonValues[id] == "-" && _state.value.isXTurn) {
            updateBoard(id, "X")
            // Check if the game is over after "X" move, if not, let "O" make a move
            if (!isGameOver() && !_state.value.isXTurn) {
                makeRandomMove()
            }
        }
    }

    private fun updateBoard(position: Int, player: String) {
        val buttons = _state.value.buttonValues.copyOf()
        buttons[position] = player
        _state.value = _state.value.copy(
            buttonValues = buttons,
            isXTurn = !_state.value.isXTurn
        )
    }

    private fun makeRandomMove() {
        val availablePositions = _state.value.buttonValues.mapIndexedNotNull { index, value ->
            if (value == "-") index else null
        }
        if (availablePositions.isNotEmpty()) {
            val randomPosition = availablePositions[Random.nextInt(availablePositions.size)]
            updateBoard(randomPosition, "O")
            isGameOver()
        }
    }

    private fun isGameOver(): Boolean {
        if(rowHasWinner(1) || rowHasWinner(2) || rowHasWinner(3) ||
            columnHasWinner(1) || columnHasWinner(2) || columnHasWinner(3) ||
            firstDiagonalHasWinner() || secondDiagonalHasWinner()) {
            return true
        }
        return false
    }

    private fun rowHasWinner(rowId: Int): Boolean {
        val third = (rowId * 3) - 1
        val second = third - 1
        val first = second - 1
        return compareSpaces(first, second, third)
    }

    private fun columnHasWinner(columnId: Int): Boolean {
        val first = columnId - 1
        val second = first + 3
        val third = first + 6
        return compareSpaces(first, second, third)
    }

    private fun firstDiagonalHasWinner(): Boolean {
        return compareSpaces(0, 4, 8)
    }

    private fun secondDiagonalHasWinner(): Boolean {
        return compareSpaces(2, 4, 6)
    }

    private fun compareSpaces(first: Int, second: Int, third: Int): Boolean {
        if (_state.value.buttonValues[first] == "-" || _state.value.buttonValues[second] == "-" || _state.value.buttonValues[third] == "-") {
            return false
        }
        val firstTwoMatch = _state.value.buttonValues[first] == _state.value.buttonValues[second]
        val secondTwoMatch = _state.value.buttonValues[second] == _state.value.buttonValues[third]
        if (firstTwoMatch && secondTwoMatch) {
            _state.value = _state.value.copy(victor = _state.value.buttonValues[first])
            val buttonWinners = _state.value.buttonWinners.copyOf()
            buttonWinners[first] = true
            buttonWinners[second] = true
            buttonWinners[third] = true
            _state.value = _state.value.copy(buttonWinners = buttonWinners)
            return true
        }
        return false
    }

    fun resetBoard() {
        _state.value = TicTicToeState()
    }
}
