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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
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
            AlarmClockDisplay()
        }
    }

    //lightBlue color code
    private val lightBlue = Color(0xFFADD8E6)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlarmClockApp() {
        MaterialTheme {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Brain Boost | Alarm Clock", color = Color.Black) }, colors = TopAppBarDefaults.topAppBarColors(containerColor=lightBlue)) },
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
            verticalArrangement = Arrangement.Top
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

    @Composable
    fun AlarmClockDisplay() {
        // Moved state up here to share between multiple instances or other composables as needed
        var numberOfHours by remember { mutableStateOf(1) }
        var numberOfMinutes by remember { mutableStateOf(0) }
        var mSelectedText by remember { mutableStateOf("AM") }
        var mCheckedState by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Column (
            modifier = Modifier.background(lightBlue)
        ) {
            Row(
                modifier = Modifier.wrapContentSize(Alignment.TopCenter),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Your Row for Alarm 1 Name remains the same
            }

            Row{
                // Now passing the state and state update functions to the composable
                callIndividualAlarm(
                    numberOfHours = numberOfHours,
                    onHoursChange = { numberOfHours = it },
                    numberOfMinutes = numberOfMinutes,
                    onMinutesChange = { numberOfMinutes = it },
                    mSelectedText = mSelectedText,
                    onSelectedTextChange = { mSelectedText = it },
                    mCheckedState = mCheckedState,
                    onCheckedChange = { isChecked ->
                        mCheckedState = isChecked
                        val message = if (isChecked) "Alarm is turned on" else "Alarm is turned off"
                        showToastForAlarmStatus(context, message)
                        if (isChecked) {
                            // Using the hoisted state directly
                            Log.d("AlarmClock", "Entered the alarm is checked to create alarm")
                            Log.d("AlarmClock", "hour is $numberOfHours but numberOfHours is $numberOfHours")
                            Log.d("AlarmClock", "hour is $numberOfMinutes but numberOfMinutes is $numberOfMinutes")
                            createAnAlarm(
                                context = context,
                                label = "Alarm !",
                                hour = numberOfHours,
                                minute = numberOfMinutes,
                                meridian = mSelectedText, // This should reflect the latest AM/PM selection
                                status = mCheckedState
                            )

                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun callIndividualAlarm(
        numberOfHours: Int,
        onHoursChange: (Int) -> Unit,
        numberOfMinutes: Int,
        onMinutesChange: (Int) -> Unit,
        mSelectedText: String,
        onSelectedTextChange: (String) -> Unit,
        mCheckedState: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onHoursChange(numberOfHours + 1) },
                    modifier = Modifier.size(width = 90.dp, height = 40.dp)
                ) {
                    Text(text = "UP")
                }

                Text(
                    text = numberOfHours.toString(),
                    style = TextStyle(fontSize = 30.sp)
                )

                Button(
                    onClick = { onHoursChange(numberOfHours - 1) },
                    modifier = Modifier.size(width = 90.dp, height = 40.dp)
                ) {
                    Text(text = "DOWN")
                }

                Text(text = "H", color = Color.Gray)
            }

            Text(
                text = ":",
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onMinutesChange(numberOfMinutes + 1) },
                    modifier = Modifier.size(width = 90.dp, height = 40.dp)
                ) {
                    Text(text = "UP")
                }

                Text(
                    text = numberOfMinutes.toString(),
                    style = TextStyle(fontSize = 30.sp)
                )

                Button(
                    onClick = { onMinutesChange(numberOfMinutes - 1) },
                    modifier = Modifier.size(width = 90.dp, height = 40.dp)
                ) {
                    Text(text = "DOWN")
                }

                Text(text = "M", color = Color.Gray)


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var mExpanded by remember { mutableStateOf(false) }
                val mOptions = listOf("AM", "PM")
                var mSelectedText by remember { mutableStateOf("") }
                var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

                val icon = if (mExpanded)
                    Icons.Filled.KeyboardArrowUp
                else
                    Icons.Filled.KeyboardArrowDown

                Row(modifier = Modifier.padding(all = 10.dp)) {
                    //Text(text = "Row 1")
                    OutlinedTextField(value = mSelectedText, onValueChange = { /*mSelectedText = it*/ },
                        modifier = Modifier
                            .wrapContentSize()
                            .onGloballyPositioned { coordinates ->
                                mTextFieldSize = coordinates.size.toSize()
                            },
                        label = {Text("AM/PM")},
                        readOnly = true,
                        trailingIcon = {
                            Icon(icon, "contentDescription",
                                Modifier.clickable { mExpanded = !mExpanded})
                        }
                    )

                    DropdownMenu(expanded = mExpanded, onDismissRequest = { mExpanded = false }
                    ) {
                        mOptions.forEach { label ->
                            DropdownMenuItem(text = { Text(text = label) }, onClick = {
                                mSelectedText = label
                                mExpanded = false
                            })
                        }
                    }
                }

                var mCheckedState by remember { mutableStateOf(false) }
                val context = LocalContext.current

                Row {
                    Switch(
                        checked = mCheckedState,
                        onCheckedChange = onCheckedChange
                    )
                }
                }

                Row {
                    Text(text = "Off/On", color = Color.Gray)
                }
            }
        }

    }


    private fun showToastForAlarmStatus(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun createAnAlarm(context: Context, label: String, hour: Int, minute: Int, meridian: String, status: Boolean) {
        val newAlarm = Alarm(
            label = label,
            hour = hour,
            minute = minute,
            meridian = meridian,
            status = status
        )
        alarmViewModel.insertAlarm(newAlarm)
        Log.d("AlarmClock", "alarm sent to alarmviewmodel with $newAlarm")
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
