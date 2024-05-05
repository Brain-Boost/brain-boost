package com.example.brainboost.games.ttt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainboost.ringer.AlarmRing
import com.example.brainboost.ui.theme.Colors


@Composable
fun Tictactoe(
    modifier: Modifier = Modifier,
    viewModel: TicTacToeViewModel = TicTacToeViewModel(),
    alarmRing: AlarmRing // added - SJL
){
    val state = viewModel.state.value
    Column(
        modifier.background(Colors.lightBlue).fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val turn = if(state.isXTurn) "X's Turn" else "O's Turn"
        val turnMessage = "It is $turn"
        val winner = state.victor

        // alarm kill code - SJL
        if (winner != null && winner == "X") {
            if (alarmRing.isPlaying()) {
                alarmRing.stopRingtone()
            }
        }
        // Check if there is a winner or tie
        val endGameMessage = when {
            winner != null -> "$winner Wins"
            state.buttonValues.none { it == "-" } -> "It's a Tie!"
            else -> turnMessage
        }

        Text(
            text = endGameMessage,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(16.dp),
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium
        )

        // Display the current difficulty level
        Text("Current Difficulty: ${viewModel.currentDifficulty.name}")

        // Buttons to adjust the difficulty level
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.currentDifficulty = Difficulty.EASY }) {
                Text("Easy")
            }
            Button(onClick = { viewModel.currentDifficulty = Difficulty.MEDIUM }) {
                Text("Medium")
            }
            Button(onClick = { viewModel.currentDifficulty = Difficulty.HARD }) {
                Text("Hard")
            }
        }

        BuildRow(rowId = 1, viewModel = viewModel)
        BuildRow(rowId = 2, viewModel = viewModel)
        BuildRow(rowId = 3, viewModel = viewModel)

        Button(
            onClick = {viewModel.resetBoard()},
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ){
            Text(text = "Reset Game", fontSize = 20.sp)
        }

    }
}

@Composable
fun BuildRow(
    rowId: Int,
    modifier: Modifier = Modifier,
    viewModel: TicTacToeViewModel
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val third = (rowId * 3) - 1
        val second = third - 1
        val first = second - 1
        val buttonColors = viewModel.state.value.buttonWinners
        val buttonValues = viewModel.state.value.buttonValues
        TicTacToeButton(buttonValues[first],buttonColors[first]) { viewModel.setButton(first)}
        TicTacToeButton(buttonValues[second],buttonColors[second]) { viewModel.setButton(second)}
        TicTacToeButton(buttonValues[third],buttonColors[third]) { viewModel.setButton(third)}
    }
}

@Composable
fun TicTacToeButton(
    button: String,
    shouldChangeColor: Boolean,
    onClick: () -> Unit,
){
    val color = if(shouldChangeColor) MaterialTheme.colorScheme.tertiary
    else MaterialTheme.colorScheme.primary
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.large // Using the default large rounded button shape
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(80.dp) // Adjust size as needed
        ) {
            Text(
                text = button,
                fontSize = 40.sp
            )
        }
    }
}
