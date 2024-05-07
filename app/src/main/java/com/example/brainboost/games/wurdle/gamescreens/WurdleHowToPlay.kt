package com.example.brainboost.games.wurdle.gamescreens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class WurdleHowToPlay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HTP2()
        }
    }
    @Composable
    fun HTP2() {
        Text(
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp),
            fontSize = 30.sp,
            text = """
                How to play Wurdle
            """.trimIndent()
        )

        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                
                
                    Objective:
            """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(20.dp),
            fontSize = 20.sp,
            text = """
    
               
               
                    The objective of Tic Tac Toe is to get three of your symbols (X) in a row, column, or diagonal on the game board.
            """.trimIndent()
        )

        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                        
                
                
                
                
                
                    Rules:
            """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(20.dp),
            fontSize = 20.sp,
            text = """
                        
                
               
               
               
               
               
               
                    1. The game is played on a 3x3 grid.
                    2. Player 1 starts the game by placing their symbol (X) on an empty square.
                    3. Player 2 is an AI that will be placing their symbol (O) on an empty square.
                    4. Players take turns until one player achieves three of their symbols in a row, column, or diagonal, or if the board is full (resulting in a draw).
                    5. The game ends when there is a winner or a draw.
            """.trimIndent()
        )

        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                    How to Play:
            """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(20.dp),
            fontSize = 20.sp,
            text = """
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                    1. Change the AI difficulty to what you want.
                    2. Player 1 makes the first move by selecting an empty square to place their X.
                    3. Player 2 then selects an empty square to place their O.
                    4. Players continue taking turns until one player wins or the game ends in a draw.
            """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(30.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                    Have fun playing Tic Tac Toe!!!
            """.trimIndent()
        )
        /*
                        Text(
                            text = """
                        Hot to play Tic Tac Toe

                        Objective:
                        The objective of Tic Tac Toe is to get three of your symbols (X) in a row, column, or diagonal on the game board.

                        Rules:
                        1. The game is played on a 3x3 grid.
                        2. Player 1 starts the game by placing their symbol (X) on an empty square.
                        3. Player 2 is an AI that will be placing their symbol (O) on an empty square.
                        4. Players take turns until one player achieves three of their symbols in a row, column, or diagonal, or if the board is full (resulting in a draw).
                        5. The game ends when there is a winner or a draw.

                        How to Play:
                        1. Change the AI difficulty to what you want.
                        2. Player 1 makes the first move by selecting an empty square to place their X.
                        3. Player 2 then selects an empty square to place their O.
                        4. Players continue taking turns until one player wins or the game ends in a draw.

                        Have fun playing Tic Tac Toe!
                    """.trimIndent()
                        )
        */

    }
}