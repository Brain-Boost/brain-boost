package com.example.brainboost.ui.theme

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.brainboost.games.wurdle.repository.AssetFileWordRepository
import com.example.brainboost.games.wurdle.repository.LocalStorageLevelRepository
import com.example.brainboost.games.wurdle.usecase.GetNextLevel
import com.example.brainboost.games.wurdle.usecase.GetWordStatus
import com.example.brainboost.games.wurdle.usecase.ResetLevels
import com.example.brainboost.games.wurdle.viewmodel.LevelsViewModel
import com.example.brainboost.ui.theme.Colors.lightBlueBackground
import com.example.brainboost.games.wurdle.gamescreens.GameHeader
import com.example.brainboost.games.wurdle.gamescreens.WordScreen

class WurdleGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WurdleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    /*color = MaterialTheme.colorScheme.background,*/
                    color = lightBlueBackground
                ) {
                    // simple dependency injection
                    val assetWordRepository = remember {
                        AssetFileWordRepository(assets)
                    }
                    val getWordStatus = remember {
                        GetWordStatus(assetWordRepository)
                    }
                    val sharedPreferences: SharedPreferences = remember {
                        getSharedPreferences("default", MODE_PRIVATE)
                    }
                    val levelRepository = remember {
                        LocalStorageLevelRepository(sharedPreferences)
                    }

                    val getNextLevel = remember {
                        GetNextLevel(assetWordRepository, levelRepository)
                    }
                    val resetLevels = remember {
                        ResetLevels(levelRepository)
                    }
                    val levelViewModel = remember {
                        LevelsViewModel(levelRepository, getNextLevel, resetLevels)
                    }

                    val level = levelViewModel.state().collectAsState().value.currentLevel
                    if (level != null) {
                        WordScreen(level, getWordStatus) {
                            levelViewModel.levelPassed()
                        }
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                GameHeader {

                                }
                                Text(text = "You have mastered the game (1024 levels)!",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(top = 32.dp))

                                Text(
                                    text = "Want to reset to the first level?",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(top = 32.dp),
                                )
                                Button(
                                    onClick = {
                                        levelViewModel.reset()
                                    },
                                    modifier = Modifier.padding(top = 16.dp),
                                ) {
                                    Text(text = "Reset")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
