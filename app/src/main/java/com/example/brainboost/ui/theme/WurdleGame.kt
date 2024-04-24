package com.example.brainboost.ui.theme

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.brainboost.games.wurdle.gamescreens.GameHeader
import com.example.brainboost.games.wurdle.gamescreens.WordScreen
import com.example.brainboost.games.wurdle.repository.AssetFileWordRepository
import com.example.brainboost.games.wurdle.repository.LocalStorageLevelRepository
import com.example.brainboost.games.wurdle.usecase.GetNextLevel
import com.example.brainboost.games.wurdle.usecase.GetWordStatus
import com.example.brainboost.games.wurdle.usecase.ResetLevels
import com.example.brainboost.games.wurdle.viewmodel.LevelsViewModel
import com.example.brainboost.nav.FlexibleDrawer
import com.example.brainboost.nav.NavigationItem
import com.example.brainboost.ui.theme.Colors.lightBlueBackground
import kotlinx.coroutines.launch

class WurdleGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Here you get the SharedPreferences
            val sharedPreferences: SharedPreferences = getSharedPreferences("default", MODE_PRIVATE)

            // Now, use remember to store instances of your repository and use cases
            val assetWordRepository = remember { AssetFileWordRepository(assets) }
            val getWordStatus = remember { GetWordStatus(assetWordRepository) }
            val levelRepository = remember { LocalStorageLevelRepository(sharedPreferences) }
            val getNextLevel = remember { GetNextLevel(assetWordRepository, levelRepository) }
            val resetLevels = remember { ResetLevels(levelRepository) }
            val levelViewModel = remember { LevelsViewModel(levelRepository, getNextLevel, resetLevels) }

            // Define your navigation items list
            val items = listOf(
                NavigationItem("Home", Icons.Filled.Home),
                NavigationItem("Alarms", Icons.Filled.Notifications),
                NavigationItem("Tic-Tac-Toe", Icons.Filled.PlayArrow),
                NavigationItem("Memory-Game", Icons.Filled.PlayArrow)
                // Add other items here...
            )

            // Determine the selected item based on your logic
            val selectedItem = items.first()

            // Call your FlexibleDrawer and pass the necessary arguments
            FlexibleDrawer(
                context = this@WurdleGame, // Pass the Activity context
                items = items,
                selectedItem = selectedItem
            ) { drawerState ->
                // This is the main content of your drawer, which is the Wurdle game
                WurdleContent(drawerState, levelViewModel, getWordStatus)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WurdleContent(
    drawerState: DrawerState,
    levelViewModel: LevelsViewModel,
    getWordStatus: GetWordStatus
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wurdle Game") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            drawerState.open() // Open the drawer
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = lightBlueBackground // Set your desired color here
                )
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                WurdleTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = lightBlueBackground
                    ) {
                        val level = levelViewModel.state().collectAsState().value.currentLevel
                        if (level != null) {
                            WordScreen(level, getWordStatus) {
                                levelViewModel.levelPassed()
                            }
                        } else {
                            GameCompletionScreen(levelViewModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun GameCompletionScreen(levelViewModel: LevelsViewModel) {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GameHeader { }
            Text(
                text = "You have mastered the game (1024 levels)!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "Want to reset to the first level?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 32.dp),
            )
            Button(
                onClick = { levelViewModel.reset() },
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text(text = "Reset")
            }
        }
    }
}
