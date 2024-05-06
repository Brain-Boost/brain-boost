package com.example.brainboost.games.memoryGame.memoryFeature.presentation

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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            fontSize = 30.sp,
            text = """
                How To Play The Memory Game
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
            modifier = Modifier.padding(10.dp),
            fontSize = 20.sp,
            text = """
    
               
               
               
            Match the shown icon with another one that is equal. Match them all to win the game!!!
        """.trimIndent()
        )

        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                
                
                
                
                
                
            How to play:
        """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(10.dp),
            fontSize = 20.sp,
            text = """
                
               
               
               
               
               
               
               
               
            Flip the cards over one at a time to see if you get a matched pair.
            If there is a matched pair then the last flipped card gets a green border signifying that there is a match.
            If there is no match the two cards that are flipped will flip back over after some time.
            You win the game once you have matched all the cards that are shown.
        """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
            CONGRATULATIONS YOU CAN NOW PLAY THE MEMORY GAME 
            
        """.trimIndent()
        )
    }
}