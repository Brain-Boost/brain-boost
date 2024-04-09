package com.example.brainboost.memoryGame.memoryFeature.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.brainboost.memoryGame.memoryFeature.presentation.memoryScreen.MemoryScreen
import com.example.brainboost.memoryGame.memoryFeature.presentation.memoryScreen.MemoryViewModel
import com.example.brainboost.ui.theme.MemoryTheme

//add images for the actual cards both the back and the faces
class MemoryGame:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MemoryViewModel by viewModels()
                    MemoryScreen(viewModel = viewModel)
                }
            }
        }
    }
}