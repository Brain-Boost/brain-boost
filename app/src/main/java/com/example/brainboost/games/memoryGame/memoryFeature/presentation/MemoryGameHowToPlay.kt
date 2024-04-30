package com.example.brainboost.games.memoryGame.memoryFeature.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class MemoryGameHowToPlay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HTP1()
        }
    }
    @Composable
    fun HTP1() {
        Text(
            text = """
                How To play the Memory Game
                
                Objective:
                Match the shown icon with another one that is equal. Match them all to win the game.
            """.trimIndent()
        )
    }
}