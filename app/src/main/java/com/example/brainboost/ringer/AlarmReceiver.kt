package com.example.brainboost.ringer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.brainboost.Push

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Context should not be null; if it is, we cannot proceed
        context?.let {
            Log.d("AlarmClock", "Alarm received!")
            val alarmRing = AlarmRing(it)
            alarmRing.playRingtone()

            val push = Push(it)
            push.showGameNotification()

        }
    }

}