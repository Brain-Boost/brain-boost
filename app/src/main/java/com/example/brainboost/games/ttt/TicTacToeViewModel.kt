package com.example.brainboost.games.ttt

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import kotlin.random.Random

class TicTacToeViewModel : ViewModel() {
    private val _state = mutableStateOf(TicTicToeState())
    val state: State<TicTicToeState> = _state
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    var currentDifficulty by mutableStateOf(Difficulty.MEDIUM)
    private var hypothetical = false

    fun setButton(id: Int) {
        if (_state.value.victor == null && _state.value.buttonValues[id] == "-" && _state.value.isXTurn) {
            updateBoard(id, "X")
            if (!isGameOver() && !_state.value.isXTurn) {
                viewModelScope.launch {
                    delay(500)
                    makeMove()
                }
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

    private fun makeMove() {
        when (currentDifficulty) {
            Difficulty.EASY -> makeRandomMove()
            Difficulty.MEDIUM -> makeMediumMove()
            Difficulty.HARD -> makeHardMove()
        }
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

    private fun makeMediumMove() {
        val winningMove = findWinningMoveForOpponent()
        if (winningMove != null) {
            updateBoard(winningMove, "O")
            isGameOver()
        } else {
            makeRandomMove()
        }
    }

    private fun makeHardMove() {
        val bestMove = findBestMove()
        if (bestMove != null) {
            updateBoard(bestMove, "O")
            isGameOver()
        }
    }

    private fun findWinningMoveForOpponent(): Int? {
        for (position in _state.value.buttonValues.indices) {
            if (_state.value.buttonValues[position] == "-") {
                val newState = _state.value.copy(buttonValues = _state.value.buttonValues.copyOf())
                newState.buttonValues[position] = "O"
                if (isGameOver(newState)) {
                    return position
                }
            }
        }
        return null
    }

    private fun findBestMove(): Int? {
        // Evaluate winning moves
        for (position in _state.value.buttonValues.indices) {
            if (_state.value.buttonValues[position] == "-") {
                val newState = _state.value.copy(buttonValues = _state.value.buttonValues.copyOf())
                newState.buttonValues[position] = "O"
                if (isGameOver(newState)) {
                    return position
                }
            }
        }

        // Block opponent's winning moves
        for (position in _state.value.buttonValues.indices) {
            if (_state.value.buttonValues[position] == "-") {
                val newState = _state.value.copy(buttonValues = _state.value.buttonValues.copyOf())
                newState.buttonValues[position] = "X"
                hypothetical = true
                if (isGameOver(newState)) {
                    hypothetical = false
                    return position
                }
                hypothetical = false
            }
        }

        // Favor center
        if (_state.value.buttonValues[4] == "-") {
            return 4
        }

        // Favor corners
        val corners = listOf(0, 2, 6, 8)
        for (corner in corners) {
            if (_state.value.buttonValues[corner] == "-") {
                return corner
            }
        }

        // Choose random available square
        val availablePositions = _state.value.buttonValues.indices.filter { _state.value.buttonValues[it] == "-" }
        return if (availablePositions.isNotEmpty()) availablePositions.random() else null
    }

    private fun isGameOver(state: TicTicToeState = _state.value): Boolean {
        return rowHasWinner(state, 1) || rowHasWinner(state, 2) || rowHasWinner(state, 3) ||
                columnHasWinner(state, 1) || columnHasWinner(state, 2) || columnHasWinner(state, 3) ||
                firstDiagonalHasWinner(state) || secondDiagonalHasWinner(state) ||
                state.buttonValues.none { it == "-" }

    }

    private fun rowHasWinner(state: TicTicToeState, rowId: Int): Boolean {
        val third = (rowId * 3) - 1
        val second = third - 1
        val first = second - 1
        return compareSpaces(state, first, second, third)
    }

    private fun columnHasWinner(state: TicTicToeState, columnId: Int): Boolean {
        val first = columnId - 1
        val second = first + 3
        val third = first + 6
        return compareSpaces(state, first, second, third)
    }

    private fun firstDiagonalHasWinner(state: TicTicToeState): Boolean {
        return compareSpaces(state, 0, 4, 8)
    }

    private fun secondDiagonalHasWinner(state: TicTicToeState): Boolean {
        return compareSpaces(state, 2, 4, 6)
    }


    private fun compareSpaces(state: TicTicToeState, first: Int, second: Int, third: Int): Boolean {
        if (state.buttonValues[first] == "-" || state.buttonValues[second] == "-" || state.buttonValues[third] == "-") {
            return false
        }
        val firstTwoMatch = state.buttonValues[first] == state.buttonValues[second]
        val secondTwoMatch = state.buttonValues[second] == state.buttonValues[third]
        if (firstTwoMatch && secondTwoMatch) {
            if (!hypothetical) {
                val winner = state.buttonValues[first]
                _state.value = state.copy(victor = winner)
                val buttonWinners = state.buttonWinners.copyOf()
                buttonWinners[first] = true
                buttonWinners[second] = true
                buttonWinners[third] = true
                _state.value = state.copy(buttonWinners = buttonWinners)
                _state.value.victor = winner
            }
            return true
        }
        if (state.buttonValues.none { it == "-" }) {
            // Game ended in a tie
            return true
        }
        return false
    }

    fun resetBoard() {
        _state.value = TicTicToeState()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}
