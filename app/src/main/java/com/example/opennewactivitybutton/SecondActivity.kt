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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
            // AlarmClockApp()
            AlarmClockDisplay()
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
        var label by remember { mutableStateOf("") }
        var hour by remember { mutableStateOf("") }
        var minute by remember { mutableStateOf("") }
        var meridian by remember { mutableStateOf("AM") }
        var status by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }
        val focusManager = LocalFocusManager.current
        var flag: Boolean

        // Additional state to trigger Snackbar showing
        var showSnackbar by remember { mutableStateOf(false) }

        // Detect taps outside TextField to dismiss keyboard
        val modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(16.dp) // Apply padding here if needed

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier // Apply the modifier to Column
        ) {
            // Your TextFields and other UI components here
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
                    Switch(
                        checked = meridian == "PM",
                        onCheckedChange = { meridian = if (it) "PM" else "AM" })
                    Text("PM")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Alarm Status")
                    Switch(checked = status, onCheckedChange = { status = it })
                }
                Button(onClick = {
                    val hourChecked = hour.toIntOrNull()?.takeIf { it in 1..12 }
                    val minuteChecked = minute.toIntOrNull()?.takeIf { it in 0..59 }
                    if (hourChecked != null && minuteChecked != null) {
                        val newAlarm = Alarm(
                            label = label,
                            hour = hourChecked,
                            minute = minuteChecked,
                            meridian = meridian,
                            status = status
                        )
                        alarmViewModel.insertAlarm(newAlarm)
                        Log.d("AlarmCreate", "Setting Alarm: $newAlarm")
                        showSnackbar = true // Trigger Snackbar showing
                    } else {
                        // Handle validation error
                        showSnackbar = true
                        flag = true
                    }
                }) {
                    Text("Save Alarm")
                }
            }

            // React to showSnackbar state change to show the Snackbar
            if (showSnackbar) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = "Alarm created successfully",
                        actionLabel = "OK"
                    )
                    showSnackbar = false // Reset state after showing
                }
            }
            /*else if(showSnackbar and flag){
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = "Failed to create alarm",
                        actionLabel = "CLOSE"
                    )
                    showSnackbar = false // Reset state after showing
                }
            }*/

            // Display the Snackbar
            SnackbarHost(hostState = snackbarHostState)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (alarmRing.isPlaying()) {
            alarmRing.stopRingtone()
        }
    }

    //lightBlue color code
    private val lightBlue = Color(0xFFADD8E6)


    @Composable
// This function sets the layout and is able to display 3 alarms at once.
// At this point it has the ability to display 1 alarm, and I will implement
// this function in the coming sprints to complete this function.
    fun AlarmClockDisplay() {
        var label by remember { mutableStateOf("") }
        var hour by remember { mutableStateOf("") }
        var minute by remember { mutableStateOf("") }
        var meridian by remember { mutableStateOf("AM") }
        var status by remember { mutableStateOf(false) }
        Column (
            // The background of this layout keeps the same as the main page.
            modifier = Modifier.background( lightBlue )
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopCenter),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // A placeholder for user to customize their own name of this alarm.
                // Right now (by default) this name is set to Alarm 1.
                Spacer(modifier = Modifier.size(height = 30.dp, width = 30.dp))
                Text(text = "Alarm 1", color = Color.Gray)
            }

            Row{
                callIndividualAlarm()
            }
        }
    }

    @Composable
// This is the main function to display 1 alarm. This function will be called
// 3 times to display 3 alarms.
    private fun callIndividualAlarm() {
        Row(// There is 1 row with 4 elements.
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(// This column displays all elements of hours, including an up button,
                // a down button, and the number of hours.
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // This variable stores the number of hours for future use.
                var numberOfHours by remember { mutableIntStateOf(1) }

                Row(modifier = Modifier.padding(all = 10.dp)) {
                    Button(// Clicking the button will increase the number by 1.
                        // I plan to rewrite this line in a separate function in the future.
                        onClick = { numberOfHours = (numberOfHours + 1).coerceIn(1, 12) },
                        modifier = Modifier.size(width = 90.dp, height = 40.dp)
                    ) {
                        Text(text = "UP")
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row {
                    Box(// This box contains the number of hours.
                        modifier = Modifier.size(width = 60.dp, height = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = numberOfHours.toString(),
                            style = TextStyle(fontSize = 30.sp)
                        )
                    }

                }

                Spacer(modifier = Modifier.width(10.dp))

                Row(modifier = Modifier.padding(all = 10.dp)) {
                    Button(// Clicking the button will decrease the number by 1.
                        // I plan to rewrite this line in a separate function in the future.
                        onClick = { numberOfHours = (numberOfHours - 1).coerceIn(1, 12) },
                        modifier = Modifier.size(width = 90.dp, height = 40.dp)
                    ) {
                        Text(text = "DOWN")
                    }
                }

                Row {// Displays a gray H at the bottom of this column.
                    Text(text = "H", color = Color.Gray)
                }

            }

            Column{
                Text(
                    text = ":",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }

            Column(// This column displays all elements of minutes, including an up button,
                // a down button, and the number of minutes.
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // This variable stores the number of hours for future use.
                var numberOfMinutes by remember { mutableIntStateOf(0) }

                Row(modifier = Modifier.padding(all = 10.dp)) {
                    Button(// Clicking the button will increase the number by 1.
                        // I plan to rewrite this line in a separate function in the future.
                        onClick = { numberOfMinutes = (numberOfMinutes + 1).coerceIn(0, 59) },
                        modifier = Modifier.size(width = 90.dp, height = 40.dp)
                    ) {
                        Text(text = "UP")
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row {
                    Box(// This box contains the number of minutes.
                        modifier = Modifier.size(width = 60.dp, height = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = numberOfMinutes.toString(),
                            style = TextStyle(fontSize = 30.sp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))

                Row(modifier = Modifier.padding(all = 10.dp)) {
                    //Text(text = "Row 3")

                    Button(// Clicking the button will decrease the number by 1.
                        // I plan to rewrite this line in a separate function in the future.
                        onClick = { numberOfMinutes = (numberOfMinutes - 1).coerceIn(0, 59) },
                        modifier = Modifier.size(width = 90.dp, height = 40.dp)
                    ) {
                        Text(text = "DOWN")
                    }
                }

                Row {
                    Text(text = "M", color = Color.Gray)
                }
            }

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
                    //Text(text = "Row 2")
                    Switch(
                        checked = mCheckedState,
                        onCheckedChange = { isChecked ->
                            mCheckedState = isChecked
                            val message = if (isChecked) "Alarm is turned on" else "Alarm is turned off"
                            showToastForAlarmStatus(context, message)
                        })
                }

                Row {
                    Text(text = "Off/On", color = Color.Gray)
                }
            }
        }

    }

    fun onClick() {
        //
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
}
