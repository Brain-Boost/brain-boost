package com.example.brainboost

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainboost.database.entities.Alarm
import com.example.brainboost.ringer.AlarmRing
import com.example.brainboost.ringer.AlarmViewModel


class AlarmClock : ComponentActivity() {
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmRing: AlarmRing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // AlarmClockApp() // Assuming you'll use this in the final version
            AlarmClockDisplay(alarmViewModel)

            //DisplayLatestAlarm(alarmViewModel = alarmViewModel)
        }
    }

    private fun createAnAlarm(
        context: Context,
        label: String,
        hour: Int,
        minute: Int,
        meridian: String,
        status: Boolean
    ) {
        val newAlarm = Alarm(
            label = label,
            hour = hour,
            minute = minute,
            meridian = meridian,
            status = status
        )
        // Now, alarmViewModel should be accessible here
        alarmViewModel.insertAlarm(newAlarm)
        Log.d("AlarmClock", "alarm sent to alarmviewmodel with $newAlarm")
    }


    //lightBlue color code
    private val lightBlue = Color(0xFFADD8E6)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlarmClockApp() {
        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(
                            "Brain Boost | Alarm Clock",
                            color = Color.Black
                        )
                    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = lightBlue))
                },
                content = { padding ->
                    AlarmTopLabel(padding)
                },
                containerColor = lightBlue
            )
        }
    }


    @Composable
    fun AlarmTopLabel(padding: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text("Alarm Clock!", fontSize = 32.sp, modifier = Modifier.padding(bottom = 16.dp))
            // AlarmInputForm(alarmViewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (alarmRing.isPlaying()) {
            alarmRing.stopRingtone()
        }
    }


// This function sets the layout and is able to display 3 alarms at once.
// At this point it has the ability to display 1 alarm, and I will implement
// this function in the coming sprints to complete this function.


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlarmClockDisplay(alarmViewModel: AlarmViewModel) {
        val context = LocalContext.current
        var numberOfHours by remember { mutableStateOf(1) }
        var numberOfMinutes by remember { mutableStateOf(0) }
        var mSelectedText by remember { mutableStateOf("AM") }
        var mCheckedState by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Brain Boost | Alarm Clock") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(lightBlue)
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))

                DisplayLatestAlarm(alarmViewModel = alarmViewModel)

                callIndividualAlarm(
                    numberOfHours = numberOfHours,
                    onHoursChange = { numberOfHours = it },
                    numberOfMinutes = numberOfMinutes,
                    onMinutesChange = { numberOfMinutes = it },
                    mSelectedText = mSelectedText,
                    onSelectedTextChange = { mSelectedText = it },
                    mCheckedState = mCheckedState,
                    onCheckedChange = { mCheckedState = it }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    fun callIndividualAlarm(
        numberOfHours: Int,
        onHoursChange: (Int) -> Unit,
        numberOfMinutes: Int,
        onMinutesChange: (Int) -> Unit,
        mSelectedText: String,
        onSelectedTextChange: (String) -> Unit,
        mCheckedState: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Time selection
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeSelectionColumn("H", numberOfHours, onHoursChange)
                Text(text = ":", fontSize = 30.sp, textAlign = TextAlign.Center)
                TimeSelectionColumn("M", numberOfMinutes, onMinutesChange)
            }

            // AM/PM and On/Off switches
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AMPMSelection(mSelectedText, onSelectedTextChange)
                OnOffSwitch(mCheckedState, onCheckedChange)
            }
        }
    }

    @Composable
    fun TimeSelectionColumn(unit: String, number: Int, onNumberChange: (Int) -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { onNumberChange(number + 1) },
                modifier = Modifier.size(width = 90.dp, height = 40.dp)
            ) { Text(text = "UP") }

            Text(text = number.toString().padStart(2, '0'), style = TextStyle(fontSize = 30.sp))

            Button(
                onClick = { onNumberChange(number - 1) },
                modifier = Modifier.size(width = 90.dp, height = 40.dp)
            ) { Text(text = "DOWN") }

            Text(text = unit, color = Color.Gray)
        }
    }

    @Composable
    fun AMPMSelection(mSelectedText: String, onSelectedTextChange: (String) -> Unit) {
        var mExpanded by remember { mutableStateOf(false) }
        val mOptions = listOf("AM", "PM")

        OutlinedTextField(
            value = mSelectedText,
            onValueChange = {},
            label = { Text("AM/PM") },
            trailingIcon = {
                Icon(
                    if (mExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded }
                )
            },
            readOnly = true,
            modifier = Modifier.clickable { mExpanded = true }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false }
        ) {
            mOptions.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        onSelectedTextChange(label)
                        mExpanded = false
                    },
                    text = { Text(text = label) }
                )
            }
        }
    }

    @Composable
    fun OnOffSwitch(mCheckedState: Boolean, onCheckedChange: (Boolean) -> Unit) {
        Row {
            Text(text = "Off/On", color = Color.Gray)
            Switch(checked = mCheckedState, onCheckedChange = onCheckedChange)
        }
    }


    private fun showToastForAlarmStatus(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    @Composable
    fun DisplayLatestAlarm(alarmViewModel: AlarmViewModel) {
        val latestAlarm by alarmViewModel.latestAlarm.observeAsState()

        latestAlarm?.let { alarm ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Alarm: ${alarm.hour}:${alarm.minute} ${alarm.meridian}")
            }
        } ?: Text("No alarms set yet", modifier = Modifier.padding(16.dp))
    }
}



