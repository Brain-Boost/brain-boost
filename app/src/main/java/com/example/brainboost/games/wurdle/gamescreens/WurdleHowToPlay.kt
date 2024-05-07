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
    
               
               
                    The objective of Wurdle is to guess the correct 5-letter word within 6 rounds. Each time a level is completed, the difficulty will slightly increase.
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
                        
                
               
               
               
               
               
               
               
                    1. Start the game by taking a random guess. Enter the word using the key board provided, and hit "CHECK". Use ⌫ to delete a letter.
                    2. If a letter does not appear in the word, it will be marked red, but you can still use this letter.
                    3. If a letter appears in the word but is in the wrong position, it will be marked orange.
                    4. If a letter appears in the word and is in the correct position, it will be marked green.
                    5. Keep guessing until all grids are filled with the correct letters. Keep in mind, though, that it's likely for a letter to appear multiple times in the word.
            """.trimIndent()
        )

        Text(
            modifier = Modifier.padding(30.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            text = """
                        
                        
                        
                        
                      
                        
                        
                        
                        
                     
                        
                        
                        
                        
                        
                        
                        
     
              
                        
                    Have fun playing Wurdle!!!
            """.trimIndent()
        )
    }
}