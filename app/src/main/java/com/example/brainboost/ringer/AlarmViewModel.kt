package com.example.brainboost.ringer

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.brainboost.database.AppDatabase
import com.example.brainboost.database.entities.Alarm
import com.example.brainboost.ui.theme.AlarmCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application, label: String) : AndroidViewModel(application) {
    private val alarmCreate = AlarmCreate(application)
    private val alarmDao = AppDatabase.getDatabase(application).alarmDao()
    val latestAlarm: LiveData<Alarm?> = alarmDao.getLatestAlarm().asLiveData()
    // val alarm: LiveData<Alarm?> = alarmDao.getAlarmById(label)


    // Function to insert a new alarm into the database and schedule it
    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AlarmClock", "Entered insert alarm from create $alarm")
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

    // Function to delete the latest alarm
    fun deleteAlarm(label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = alarmDao.getAlarmById(label) ?: return@launch  // Make sure there's an alarm to delete
            alarmDao.deleteAlarm(alarm)
            alarmCreate.cancelAlarm(alarm.label)
        }
    }


}