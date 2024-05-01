package com.example.brainboost.ringer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.brainboost.database.AppDatabase
import com.example.brainboost.database.entities.Alarm
import com.example.brainboost.ui.theme.AlarmCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmCreate = AlarmCreate(application)
    private val alarmDao = AppDatabase.getDatabase(application).alarmDao()
    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAllAlarms().asLiveData()

    // Function to get a specific alarm by label
    fun getAlarmByLabel(label: String): LiveData<Alarm?> = alarmDao.getAlarmByLabel(label).asLiveData()

    // Function to insert a new alarm into the database and schedule it
    fun insertAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            alarmDao.insertAlarm(alarm)
            if (alarm.status) {
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

    // Function to delete an alarm by label
    fun deleteAlarm(label: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = alarmDao.getAlarmById(label) ?: return@launch
            alarmDao.deleteAlarm(alarm)
            alarmCreate.cancelAlarm(label)
        }
    }
}
