@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.opennewactivitybutton

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.opennewactivitybutton.ui.theme.M3NavigationDrawerTheme
import com.example.opennewactivitybutton.ui.theme.Pink80
import com.example.opennewactivitybutton.ui.theme.darkGray
import com.example.opennewactivitybutton.ui.theme.gray
import com.example.opennewactivitybutton.ui.theme.redOrange
import com.example.opennewactivitybutton.ui.theme.transparent
import com.example.opennewactivitybutton.ui.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M3NavigationDrawerTheme {
                val items = listOf(
                    NavigationItem(
                        title = "Home Page",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                    ),
                    NavigationItem(
                        title = "About Us",
                        selectedIcon = Icons.Filled.Info,
                        unselectedIcon = Icons.Outlined.Info,
                        badgeCount = 45
                    ),
                    NavigationItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                    ),
                )
                    NavigationItem(
                        title = "Tic Tac Toe",
                        selectedIcon = Icons.Filled.PlayArrow,
                        unselectedIcon = Icons.Outlined.PlayArrow,
                    )
                    NavigationItem(
                        title = "Memorization Game",
                        selectedIcon = Icons.Filled.PlayArrow,
                        unselectedIcon = Icons.Outlined.PlayArrow,
                    )
                    NavigationItem(
                        title = "Wurdle",
                        selectedIcon = Icons.Filled.PlayArrow,
                        unselectedIcon = Icons.Outlined.PlayArrow,
                    )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var selectedItemIndex by rememberSaveable {
                        mutableIntStateOf(0)
                    }
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                items.forEachIndexed { index, item ->
                                    NavigationDrawerItem(
                                        label = {
                                            Text(text = item.title)
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
//                                            navController.navigate(item.route)
                                            selectedItemIndex = index
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        badge = {
                                            item.badgeCount?.let {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        },
                        drawerState = drawerState
                    ) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text(text = "Brain Boost")
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Menu"
                                            )
                                        }
                                    }
                                )
                            }
                        ) {
                        }
                    }
                }
            }
            // Calling the composable function
            // to display element and its contents
            //MainContent()
            WorkingClock()
            TicTacToeButton()
        }
    }
}
@Composable
fun TicTacToeButton(){

    // Fetching the Local Context
    val myContext = LocalContext.current

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {

        // Creating a Button that on-click
        // implements an Intent to go to Tic Tac Toe
        Button(onClick = {
            myContext.startActivity(Intent(myContext, TicTacToe::class.java))
        },
            colors = ButtonDefaults.buttonColors(Pink80), modifier = Modifier.size(300.dp, 60.dp)
        ) {
            Text("Tic Tac Toe", color = Color.White)
        }
    }
}


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)


/*
// Creating a composable
// function to display Top Bar
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
    var currentTimeInMs by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(key1 = true){
        while(true){
            delay(200)
            currentTimeInMs = System.currentTimeMillis()//clock time
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Brain Boost", color = Color.Black) }) },
        content = {
            Column (
                modifier = Modifier.padding(16.dp)
            ){
                MyContent()
                //Clock(time = { currentTimeInMs }, circleRadius = 150f, outerCircleThickness = 25f)
            }
            //MyContent()
        }

    /
*/

// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content in the above function
/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
    //time for the clock
    var currentTimeInMs by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(key1 = true){
        while(true){
            delay(200)
            currentTimeInMs = System.currentTimeMillis()//clock time
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Brain Boost", color = Color.Black) }) },
        content = {
            Column (
                modifier = Modifier.padding(16.dp)
            ){
                MyContent()
                //Clock(time = { currentTimeInMs }, circleRadius = 150f, outerCircleThickness = 25f)
            }
            //MyContent()
        }

    )



}*/


// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content in the above function
@Composable
fun WorkingClock(){
    // Fetching the Local Context
    val myContext = LocalContext.current
    var currentTimeInMs by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(key1 = true){
        while(true){
            delay(200)
            currentTimeInMs = System.currentTimeMillis()//clock time
        }
    }

    Column(Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Creating a Button that on-click
        // implements an Intent to go to SecondActivity
        Button(onClick = {
            myContext.startActivity(Intent(myContext, AlarmClock::class.java))
        },
            colors = ButtonDefaults.buttonColors(transparent),
            modifier = Modifier.size(300.dp, 300.dp)
        ) {
            //Text("Alarm Clock", color = Color.White)
            ClockSizing(time = { currentTimeInMs },
                circleRadius = 350f,
                outerCircleThickness = 25f)
        }
    }
}


@Composable
fun ClockSizing(
    modifier: Modifier = Modifier,
    time:()->Long,
    circleRadius:Float,
    outerCircleThickness:Float,
) {

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    Box(
        modifier = modifier
    ){
        Canvas(
            modifier = Modifier
                .fillMaxSize()

        ){
            //val canvasQuadrantSize = size/2f
            // inset(horizontal = 250f, vertical = 50f) {

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

enum class ClockHands {
    Seconds,
    Minutes,
    Hours
}