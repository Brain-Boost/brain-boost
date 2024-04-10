package com.example.brainboost.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.brainboost.TicTacToeViewModel
import com.example.brainboost.Tictactoe
import com.example.brainboost.ringer.AlarmRing

class TicTacToeOpener : ComponentActivity() {
    private lateinit var alarmRing: AlarmRing // had to add, otherwise alarm kill code crashes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmRing = AlarmRing.getInstance(this) // added - SJL
        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = TicTacToeViewModel()
                    Tictactoe(viewModel = viewModel, alarmRing = alarmRing)
                }
            }
        }
    }
}
