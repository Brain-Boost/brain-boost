@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.brainboost

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainboost.memoryGame.memoryFeature.presentation.MemoryGame
import com.example.brainboost.nav.FlexibleDrawer
import com.example.brainboost.nav.NavigationItem
import com.example.brainboost.ui.theme.Colors
import com.example.brainboost.ui.theme.TicTacToeOpener
import com.example.brainboost.ui.theme.brightBlue
import com.example.brainboost.ui.theme.darkGray
import com.example.brainboost.ui.theme.gray
import com.example.brainboost.ui.theme.redOrange
import com.example.brainboost.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
class HomePage : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf(
                NavigationItem("Alarms", Icons.Filled.Notifications),
                NavigationItem("Tic-Tac-Toe", Icons.Filled.PlayArrow),
                NavigationItem("Memory-Game", Icons.Filled.PlayArrow)
                // Add other items here...
            )
            val selectedItem = items.first()

            FlexibleDrawer(
                context = this@HomePage,
                items = items,
                selectedItem = selectedItem
            ) { drawerState ->
                HomeContent(drawerState)
            }
        }
    }


    @Composable
    fun HomeContent(drawerState: DrawerState) {
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Brain Boost") },
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
                    // Your existing UI elements, positioned inside the Box
                    ButtonStack()
                    // Text("Games") // Trying to add 
                    WorkingClock()
                }
            }
        )
    }

    @Composable
    fun ButtonStack(
        modifier: Modifier = Modifier
    ) {
        val myContext = LocalContext.current
        Box(
            modifier = modifier.fillMaxSize()
                .background(Colors.lightBlueBackground)
                .padding(top = 150.dp, bottom = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(.7f)
            ) {
                Row(
                    modifier = modifier.background(Colors.lightBlueBackground)
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            myContext.startActivity(
                                Intent(
                                    myContext,
                                    TicTacToeOpener::class.java
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(brightBlue),
                        modifier = Modifier.size(600.dp, 80.dp)
                    ) {
                        Text(
                            text = "Tic Tac Toe",
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    }
                }
                Row(
                    modifier = modifier.background(Colors.lightBlueBackground)
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            myContext.startActivity(
                                Intent(
                                    myContext,
                                    MemoryGame::class.java
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(brightBlue),
                        modifier = Modifier.size(600.dp, 80.dp)
                    ) {
                        Text(
                            text = "Memory Game",
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun WorkingClock() {
        // Fetching the Local Context
        val myContext = LocalContext.current
        var currentTimeInMs by remember {
            mutableLongStateOf(System.currentTimeMillis())
        }

        LaunchedEffect(key1 = true) {
            while (true) {
                delay(200)
                currentTimeInMs = System.currentTimeMillis()//clock time
            }
        }

        Column(
            Modifier.fillMaxSize()
                .padding(top = 40.dp, bottom = 4.dp), // changed top from 100 to 40 to fix inclusion of drawer
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Button(
                onClick = {
                    myContext.startActivity(Intent(myContext, AlarmClock::class.java))
                },
                colors = ButtonDefaults.buttonColors(white),
                modifier = Modifier.size(250.dp, 250.dp)
            ) {
                //Text("Alarm Clock", color = Color.White)
                ClockSizing(
                    time = { currentTimeInMs },
                    circleRadius = 350f,
                    outerCircleThickness = 25f
                )
            }
        }
    }


    @Composable
    fun ClockSizing(
        modifier: Modifier = Modifier,
        time: () -> Long,
        circleRadius: Float,
        outerCircleThickness: Float,
    ) {

        var circleCenter by remember {
            mutableStateOf(Offset.Zero)
        }
        Box(
            modifier = modifier
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()

            ) {

                val width = size.width
                val height = size.height
                circleCenter = Offset(x = width / 2f, y = height / 2f)
                val date = Date(time())
                val cal = Calendar.getInstance()
                cal.time = date
                val hours = cal.get(Calendar.HOUR_OF_DAY)
                val minutes = cal.get(Calendar.MINUTE)
                val seconds = cal.get(Calendar.SECOND)



                drawCircle(
                    style = Stroke(
                        width = outerCircleThickness
                    ),
                    brush = Brush.linearGradient(
                        listOf(
                            white.copy(0.45f),
                            darkGray.copy(0.35f)
                        )
                    ),
                    radius = circleRadius + outerCircleThickness / 2f,
                    center = circleCenter
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(
                            white.copy(0.45f),
                            darkGray.copy(0.25f)
                        )
                    ),
                    radius = circleRadius,
                    center = circleCenter
                )
                drawCircle(
                    color = gray,
                    radius = 15f,
                    center = circleCenter
                )

                val littleLineLength = circleRadius * 0.1f
                val largeLineLength = circleRadius * 0.2f
                for (i in 0 until 60) {
                    val angleInDegrees = i * 360f / 60
                    val angleInRad = angleInDegrees * PI / 180f + PI / 2f
                    val lineLength = if (i % 5 == 0) largeLineLength else littleLineLength
                    val lineThickness = if (i % 5 == 0) 5f else 2f

                    val start = Offset(
                        x = (circleRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                        y = (circleRadius * sin(angleInRad) + circleCenter.y).toFloat()
                    )

                    val end = Offset(
                        x = (circleRadius * cos(angleInRad) + circleCenter.x).toFloat(),
                        y = (circleRadius * sin(angleInRad) + lineLength + circleCenter.y).toFloat()
                    )
                    rotate(
                        angleInDegrees + 180,
                        pivot = start
                    ) {
                        drawLine(
                            color = gray,
                            start = start,
                            end = end,
                            strokeWidth = lineThickness.dp.toPx()
                        )
                    }
                }

                val clockHands = listOf(ClockHands.Seconds, ClockHands.Minutes, ClockHands.Hours)

                clockHands.forEach { clockHand ->
                    val angleInDegrees = when (clockHand) {
                        ClockHands.Seconds -> {
                            seconds * 360f / 60f
                        }

                        ClockHands.Minutes -> {
                            (minutes + seconds / 60f) * 360f / 60f
                        }

                        ClockHands.Hours -> {
                            (((hours % 12) / 12f * 60f) + minutes / 12f) * 360f / 60f
                        }
                    }
                    //clock hands sizes
                    val lineLength = when (clockHand) {
                        ClockHands.Seconds -> {
                            circleRadius * 0.9f
                        }

                        ClockHands.Minutes -> {
                            circleRadius * 0.8f
                        }

                        ClockHands.Hours -> {
                            circleRadius * 0.5f
                        }
                    }
                    val lineThickness = when (clockHand) {
                        ClockHands.Seconds -> {
                            3f
                        }

                        ClockHands.Minutes -> {
                            5f
                        }

                        ClockHands.Hours -> {
                            5f
                        }
                    }
                    val start = Offset(
                        x = circleCenter.x,
                        y = circleCenter.y
                    )

                    val end = Offset(
                        x = circleCenter.x,
                        y = lineLength + circleCenter.y
                    )
                    rotate(
                        angleInDegrees - 180,
                        pivot = start
                    ) {
                        drawLine(
                            color = if (clockHand == ClockHands.Seconds) redOrange else gray,
                            start = start,
                            end = end,
                            strokeWidth = lineThickness.dp.toPx()
                        )
                    }
                }
                // }
            }
        }
    }
}

enum class ClockHands {
    Seconds,
    Minutes,
    Hours
}