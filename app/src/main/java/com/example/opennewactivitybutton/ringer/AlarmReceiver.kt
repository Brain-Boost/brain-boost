package com.example.opennewactivitybutton.ringer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Context should not be null; if it is, we cannot proceed
        context?.let {
            Log.d("AlarmReceiver", "Alarm just fired")
            val alarmRing = AlarmRing(it)
            alarmRing.playRingtone()

        }
    }
}