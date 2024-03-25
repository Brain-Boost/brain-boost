package com.example.opennewactivitybutton

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opennewactivitybutton.database.entities.Alarm
import com.example.opennewactivitybutton.ringer.AlarmRing
import com.example.opennewactivitybutton.ringer.AlarmViewModel

class AlarmClock : ComponentActivity() {
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmRing: AlarmRing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlarmClockApp()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlarmClockApp() {
        MaterialTheme {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Brain Boost | Alarm Clock", color = Color.Black) }) },
                content = { padding ->
                    AlarmScreen(padding)
                }
            )
        }
    }

    @Composable
    fun AlarmScreen(padding: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Alarm Clock!", fontSize = 32.sp, modifier = Modifier.padding(bottom = 16.dp))
            AlarmInputForm(alarmViewModel)
        }
    }

    @Composable
    fun AlarmInputForm(alarmViewModel: AlarmViewModel) {
        // State variables to hold form input
        var label by remember { mutableStateOf("") }
        var hour by remember { mutableStateOf("") }
        var minute by remember { mutableStateOf("") }
        var meridian by remember { mutableStateOf("AM") }
        var status by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Label") }
            )
            OutlinedTextField(
                value = hour,
                onValueChange = { hour = it },
                label = { Text("Hour") }
            )
            OutlinedTextField(
                value = minute,
                onValueChange = { minute = it },
                label = { Text("Minute") }
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("AM")
                Switch(checked = meridian == "PM", onCheckedChange = { meridian = if (it) "PM" else "AM" })
                Text("PM")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Alarm Status")
                Switch(checked = status, onCheckedChange = { status = it })
            }
            Button(onClick = {
                val newAlarm = Alarm(
                    label = label,
                    hour = hour.toIntOrNull() ?: 0,
                    minute = minute.toIntOrNull() ?: 0,
                    meridian = meridian,
                    status = status
                )
                Log.d("AlarmCreate", "Setting Alarm: Label = $label, Hour = $hour, Minute = $minute, Meridian = $meridian, Status = $status")
                alarmViewModel.insertAlarm(newAlarm)
            }) {
                Text("Save Alarm")
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (alarmRing.isPlaying()) {
            alarmRing.stopRingtone()
        }
    }


    // permissions check to set an alarm
    private fun checkScheduleExactAlarmPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            // Permission not needed for older versions
            true
        }
    }
    private fun requestScheduleExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent) // Use context to call startActivity
        }
    }
}
