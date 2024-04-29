/*
Note to Self: Currently, the activity displays 3 separate alarm wheels. All three work separately.
What has broken is the display of the current alarm and the deletion of alarms. Alarm deletion
ideally still only works on the most recent alarm regardless of if it is tied to that box. I need
to refactor that whole bit of code that I was lazy about before and replace it with actual fetch code
based on alarm label. May need to pass as parameter if I'm not already.
*/

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainboost.database.entities.Alarm
import com.example.brainboost.nav.FlexibleDrawer
import com.example.brainboost.nav.NavigationItem
import com.example.brainboost.ringer.AlarmRing
import com.example.brainboost.ringer.AlarmViewModel
import com.example.brainboost.ui.theme.Colors
import kotlinx.coroutines.launch


class AlarmClock : ComponentActivity() {
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmRing: AlarmRing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf(
                NavigationItem("Home", Icons.Filled.Home),
                NavigationItem("Tic-Tac-Toe", Icons.Filled.PlayArrow),
                NavigationItem("Memory-Game", Icons.Filled.PlayArrow),
                NavigationItem("Wurdle", Icons.Filled.PlayArrow)
                // Add other items here...
            )
            val selectedItem = items.first()

            FlexibleDrawer(
                context = this@AlarmClock,
                items = items,
                selectedItem = selectedItem
            ) { drawerState ->
                AlarmContent(drawerState, alarmViewModel)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlarmContent(drawerState: DrawerState, alarmViewModel: AlarmViewModel) {
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Brain Boost | Alarm Clock") },
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
                        containerColor = Colors.lightBlueBackground
                    )
                )
            },
            content = { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    // Your existing UI elements, positioned inside the Box
                    AlarmClockDisplay(this@AlarmClock.alarmViewModel)
                }
            }
        )
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


// This function sets the layout and is able to display 3 alarms at once.
// At this point it has the ability to display 1 alarm, and I will implement
// this function in the coming sprints to complete this function.


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AlarmClockDisplay(alarmViewModel: AlarmViewModel) {


        // State variables for three alarms
        var firstAlarmHours by remember { mutableStateOf(1) }
        var firstAlarmMinutes by remember { mutableStateOf(0) }
        var firstAlarmSelectedText by remember { mutableStateOf("AM") }
        var firstAlarmCheckedState by remember { mutableStateOf(false) }

        var secondAlarmHours by remember { mutableStateOf(2) }
        var secondAlarmMinutes by remember { mutableStateOf(0) }
        var secondAlarmSelectedText by remember { mutableStateOf("AM") }
        var secondAlarmCheckedState by remember { mutableStateOf(false) }

        var thirdAlarmHours by remember { mutableStateOf(3) }
        var thirdAlarmMinutes by remember { mutableStateOf(0) }
        var thirdAlarmSelectedText by remember { mutableStateOf("AM") }
        var thirdAlarmCheckedState by remember { mutableStateOf(false) }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .background(lightBlue)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // This is where we call the individual alarm composables for three alarms
            DisplayAlarm(label = "Alarm !", alarmViewModel = alarmViewModel)
            callIndividualAlarm(
                label = "Alarm !",
                numberOfHours = firstAlarmHours,
                onHoursChange = { firstAlarmHours = it },
                numberOfMinutes = firstAlarmMinutes,
                onMinutesChange = { firstAlarmMinutes = it },
                mSelectedText = firstAlarmSelectedText,
                onSelectedTextChange = { firstAlarmSelectedText = it },
                mCheckedState = firstAlarmCheckedState,
                onCheckedChange = {
                    firstAlarmCheckedState = it
                    createAnAlarm(
                        context = context,
                        label = "Alarm !",
                        hour = firstAlarmHours,
                        minute = firstAlarmMinutes,
                        meridian = firstAlarmSelectedText,
                        status = firstAlarmCheckedState
                    )
                },
                context = context,
                alarmViewModel = alarmViewModel
            )

            DisplayAlarm(label = "Alarm Y", alarmViewModel = alarmViewModel)
            callIndividualAlarm(
                label  = "Alarm Y",
                numberOfHours = secondAlarmHours,
                onHoursChange = { secondAlarmHours = it },
                numberOfMinutes = secondAlarmMinutes,
                onMinutesChange = { secondAlarmMinutes = it },
                mSelectedText = secondAlarmSelectedText,
                onSelectedTextChange = { secondAlarmSelectedText = it },
                mCheckedState = secondAlarmCheckedState,
                onCheckedChange = {
                    secondAlarmCheckedState = it
                    createAnAlarm(
                        context = context,
                        label = "Alarm Y",
                        hour = secondAlarmHours,
                        minute = secondAlarmMinutes,
                        meridian = secondAlarmSelectedText,
                        status = secondAlarmCheckedState
                    )
                },
                context = context,
                alarmViewModel = alarmViewModel
            )

            DisplayAlarm(label = "Alarm Z", alarmViewModel = alarmViewModel)
            callIndividualAlarm(
                label  = "Alarm Z",
                numberOfHours = thirdAlarmHours,
                onHoursChange = { thirdAlarmHours = it },
                numberOfMinutes = thirdAlarmMinutes,
                onMinutesChange = { thirdAlarmMinutes = it },
                mSelectedText = thirdAlarmSelectedText,
                onSelectedTextChange = { thirdAlarmSelectedText = it },
                mCheckedState = thirdAlarmCheckedState,
                onCheckedChange = {
                    thirdAlarmCheckedState = it
                    createAnAlarm(
                        context = context,
                        label = "Alarm Z",
                        hour = thirdAlarmHours,
                        minute = thirdAlarmMinutes,
                        meridian = thirdAlarmSelectedText,
                        status = thirdAlarmCheckedState
                    )
                },
                context = context,
                alarmViewModel = alarmViewModel
            )
        }
    }

    @Composable
    fun callIndividualAlarm(
        label: String,
        numberOfHours: Int,
        numberOfMinutes: Int,
        mSelectedText: String,
        mCheckedState: Boolean,
        onHoursChange: (Int) -> Unit,
        onMinutesChange: (Int) -> Unit,
        onSelectedTextChange: (String) -> Unit,
        onCheckedChange: (Boolean) -> Unit,
        context: Context,
        alarmViewModel: AlarmViewModel
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Time selection
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeSelectionColumn(
                    label = "H",
                    range = 1..12,
                    selectedNumber = numberOfHours,
                    onNumberChange = onHoursChange
                )
                Text(text = ":", fontSize = 30.sp, textAlign = TextAlign.Center)
                TimeSelectionColumn(
                    label = "M",
                    range = 0..59,
                    selectedNumber = numberOfMinutes,
                    onNumberChange = onMinutesChange
                )
            }

            // AM/PM and On/Off switches
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Add padding if necessary
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AMPMSelection(mSelectedText, onSelectedTextChange)
                OnOffSwitch(label, mCheckedState, onCheckedChange, alarmViewModel)
            }
        }
    }


    @Composable
    fun TimeSelectionColumn(
        label: String,
        range: IntRange,
        selectedNumber: Int,
        onNumberChange: (Int) -> Unit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = TextStyle(fontSize = 24.sp),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 4.dp)
            )
            val listState = rememberLazyListState(
                initialFirstVisibleItemIndex = selectedNumber - range.first
            )
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .width(70.dp)
                    .height(150.dp)
            ) {
                items(range.toList()) { number ->
                    val isSelected = number == selectedNumber
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)  // Set a fixed height for each item
                            .clickable { onNumberChange(number) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = number.toString().padStart(2, '0'),
                            style = TextStyle(
                                fontSize = if (isSelected) 26.sp else 24.sp,  // Increase font size for selected item
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal  // Bold for selected item
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun AMPMSelection(mSelectedText: String, onSelectedTextChange: (String) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            //modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(text = "AM/PM: ", color = Color.Gray, modifier = Modifier.padding(end = 8.dp))
            // AM if the switch is checked, PM if not
            Switch(
                checked = mSelectedText == "PM",
                onCheckedChange = { isChecked ->
                    onSelectedTextChange(if (isChecked) "PM" else "AM")
                }
            )
            Text(text = if (mSelectedText == "PM") "PM" else "AM")
        }
    }


    @Composable
    fun OnOffSwitch(
        label: String,
        mCheckedState: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        alarmViewModel: AlarmViewModel
    ) {
        val context = LocalContext.current
        val Alarm by alarmViewModel.latestAlarm.observeAsState()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(text = "Off/On", color = Color.Gray, modifier = Modifier.padding(end = 8.dp))
            Switch(
                checked = mCheckedState,
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked)
                    if (!isChecked) {
                        // If the switch is turned off, delete the associated alarm
                        Alarm?.let {
                            alarmViewModel.deleteAlarm(label)
                            Toast.makeText(
                                context,
                                "Deleted Alarm: ${it.hour}:${it.minute} ${it.meridian}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
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
    fun DisplayAlarm(alarmViewModel: AlarmViewModel, label: Any) {
        val latestAlarm by alarmViewModel.latestAlarm.observeAsState()
        val context = LocalContext.current

        latestAlarm?.let { alarm ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Current Alarm: ${alarm.hour}:${alarm.minute} ${alarm.meridian}")


            }
        } ?: Text("No alarms set yet", modifier = Modifier.padding(16.dp))
    }



}

