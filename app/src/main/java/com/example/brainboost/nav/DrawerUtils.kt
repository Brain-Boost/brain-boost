package com.example.brainboost.nav

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

import com.example.brainboost.ui.theme.Colors
import com.example.brainboost.HomePage
import com.example.brainboost.AlarmClock
import com.example.brainboost.games.ttt.TTTHowToPlay
import com.example.brainboost.memoryGame.memoryFeature.presentation.MemoryGame
import com.example.brainboost.ui.theme.TicTacToeOpener
import com.example.brainboost.ui.theme.WurdleGame

@Composable
fun FlexibleDrawer(
    context: Context,
    items: List<NavigationItem>,
    selectedItem: NavigationItem,
    content: @Composable (DrawerState) -> Unit
) {
    // Create the drawer state here
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Use a Box with a specified width for the drawer content
            Box(
                modifier = Modifier
                    .background(Colors.lightBlue)
                    .fillMaxHeight()
                    .width(with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp / 2 })
            ) {
                // Your drawer content
                DrawerContent(context, items, selectedItem) { item ->
                    // Navigation logic will be here
                    when (item.title) {
                        "Home" -> {
                            context.startActivity(Intent(context, HomePage::class.java))
                        }
                        "Alarms" -> {
                            context.startActivity(Intent(context, AlarmClock::class.java))
                        }
                        "Tic-Tac-Toe" -> {
                            context.startActivity(Intent(context, TicTacToeOpener::class.java))
                        }
                        "Memory-Game" -> {
                            context.startActivity(Intent(context, MemoryGame::class.java))
                        }
                        "Wurdle" -> {
                            context.startActivity(Intent(context, WurdleGame::class.java))
                        }
                        "How to Play" -> {
                            context.startActivity(Intent(context, TTTHowToPlay::class.java))
                        }
                        // Add other cases for different navigation items
                    }
                }
            }
        },
        content = {
            content(drawerState)
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    context: Context, // Accept the context here
    items: List<NavigationItem>,
    selectedItem: NavigationItem,
    onItemSelect: (NavigationItem) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            val textStyle = if (isSelected) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
            val iconColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onItemSelect(item) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = iconColor
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = textStyle
                )
                item.badgeCount?.let {
                    Spacer(modifier = Modifier.weight(1f))
                    Badge { Text(it.toString()) }
                }
            }
        }
    }
}

