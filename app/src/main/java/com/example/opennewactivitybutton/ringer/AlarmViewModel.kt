package com.example.opennewactivitybutton.ringer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.opennewactivitybutton.database.AppDatabase
import com.example.opennewactivitybutton.database.entities.Alarm
import com.example.opennewactivitybutton.ui.theme.AlarmCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmCreate = AlarmCreate(application)
    private val alarmDao = AppDatabase.getDatabase(application).alarmDao()

    // Function to insert a new alarm into the database and schedule it
    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDao.insertAlarm(alarm)
            if (alarm.status) {
                // Schedule the alarm
                alarmCreate.setAlarm(alarm)
            }
        }
    }

    // Function to update an existing alarm
    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDao.updateAlarm(alarm)
            if (alarm.status) {
                alarmCreate.setAlarm(alarm)
            } else {
                alarmCreate.cancelAlarm(alarm.label)
            }
        }
    }

    // Function to toggle an alarm's status
    fun toggleAlarmStatus(label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = alarmDao.getAlarmById(label) ?: return@launch
            val updatedAlarm = alarm.copy(status = !alarm.status)
            alarmDao.insertAlarm(updatedAlarm) // Use insert for upsert functionality
            if (updatedAlarm.status) {
                alarmCreate.setAlarm(updatedAlarm)
            } else {
                alarmCreate.cancelAlarm(label)
            }
        }
    }
}
