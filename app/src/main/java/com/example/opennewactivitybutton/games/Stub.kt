package com.example.brainboost.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

class Stub : ComponentActivity() {
    // This is a placeholder game to prove that an alarm can open up an activity.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Text(text = "Game Over")
        }
    }

}