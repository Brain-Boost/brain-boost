package com.example.brainboost.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
import com.example.brainboost.games.ttt.TicTacToeViewModel
import com.example.brainboost.games.ttt.Tictactoe
import com.example.brainboost.nav.FlexibleDrawer
import com.example.brainboost.nav.NavigationItem
import com.example.brainboost.ringer.AlarmRing
import kotlinx.coroutines.launch

class TicTacToeOpener : ComponentActivity() {
    private lateinit var alarmRing: AlarmRing // had to add, otherwise alarm kill code crashes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmRing = AlarmRing.getInstance(this) // added - SJL
        setContent {
            val items = listOf(
                NavigationItem("Home", Icons.Filled.Home),
                NavigationItem("Alarms", Icons.Filled.Notifications),
                NavigationItem("Memory-Game", Icons.Filled.PlayArrow),
                NavigationItem("Wurdle", Icons.Filled.PlayArrow),
                NavigationItem("How to play Tic Tac Toe", Icons.Filled.Info)
                // Add other items here...
            )
            val selectedItem = items.first()

            FlexibleDrawer(
                context = this@TicTacToeOpener,
                items = items,
                selectedItem = selectedItem
            ) { drawerState ->
                TTTContent(drawerState, alarmRing)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TTTContent(drawerState: DrawerState, alarmRing: AlarmRing) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tic-Tac-Toe") },
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
                    containerColor = Colors.lightBlueBackground // Set your desired color here
                )
            )
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
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
    )
}


