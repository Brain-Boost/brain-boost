@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.opennewactivitybutton

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.opennewactivitybutton.database.AppDatabase
import com.example.opennewactivitybutton.database.entities.Alarm
import com.example.opennewactivitybutton.ui.theme.Pink80

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /////////////////////////////////////////////////////////////////
        // Database Creation
        val db = Room.databaseBuilder( // instantiate it
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build() // allowMainThreadQueries is not recommended for production use
        val alarmDao = db.AlarmDao()

        // Create a new Alarm object
        val newAlarm = Alarm(label = "Breakfast", hour=2, minute=0, meridian="PM", status=false)
        // Insert the Alarm into the database
        alarmDao.insertAlarm(newAlarm)
        // Retrieve the Alarm from the database
        val alarm = alarmDao.getAlarmById(newAlarm.label) // local var
        /////////////////////////////////////////////////////////////////

        setContent {
            // Calling the composable function
            // to display element and its contents
            MainContent()
        }
    }
}

// Creating a composable
// function to display Top Bar
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Brain Boost", color = Color.Black) }) },
            content = { MyContent() }
        )
}

// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content in the above function
@Composable
fun MyContent(){

    // Fetching the Local Context
    val myContext = LocalContext.current

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        // Creating a Button that on-click
        // implements an Intent to go to SecondActivity
        Button(onClick = {
            myContext.startActivity(Intent(myContext, AlarmClock::class.java))
        },
            colors = ButtonDefaults.buttonColors(Pink80), modifier = Modifier.size(300.dp, 60.dp)
        ) {
            Text("Alarm Clock", color = Color.White)
        }
    }
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}