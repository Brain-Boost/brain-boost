package com.example.brainboost.memoryGame.memoryFeature.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.brainboost.memoryGame.memoryFeature.presentation.memoryScreen.MemoryScreen
import com.example.brainboost.memoryGame.memoryFeature.presentation.memoryScreen.MemoryViewModel
import com.example.brainboost.nav.FlexibleDrawer
import com.example.brainboost.nav.NavigationItem
import com.example.brainboost.ui.theme.Colors
import com.example.brainboost.ui.theme.MemoryTheme
import kotlinx.coroutines.launch

//add images for the actual cards both the back and the faces
class MemoryGame:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf(
                NavigationItem("Home", Icons.Filled.Home),
                NavigationItem("Alarms", Icons.Filled.Notifications),
                NavigationItem("Tic-Tac-Toe", Icons.Filled.PlayArrow)
                // Add other items here...
            )
            val selectedItem = items.first()
            val viewModel: MemoryViewModel by viewModels()
            FlexibleDrawer(
                context = this@MemoryGame,
                items = items,
                selectedItem = selectedItem
            ) { drawerState ->
                MemoryGameContent(drawerState, viewModel)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameContent(drawerState: DrawerState, viewModel: MemoryViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Memory Flip") },
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
                    containerColor = Colors.memBlueBackground // Set your desired color here
                )
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                MemoryTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MemoryScreen(viewModel = viewModel)
                    }
                }
            }
        }
    )
}