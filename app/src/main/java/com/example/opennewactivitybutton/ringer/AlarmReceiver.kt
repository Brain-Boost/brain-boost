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

            /*val notificationHelper = NotificationCreation(it)
            notificationHelper.createNotificationChannel()
            notificationHelper.showAlarmNotification()
            val stubIntent = Intent(it, Stub::class.java).apply {
                // Required when starting an Activity from a BroadcastReceiver
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            it.startActivity(stubIntent)
            */
            /*
            // instead of forcing a game open, throw the game links (like above) as a choice they can click
            val gamesIntent = Intent(Intent.ACTION_SEND)
            val chooser = Intent.createChooser(intent, "GAMES")
            try {
                it.startActivity(chooser)
            } catch (e: ActivityNotFoundException) {
                // Define what your app should do if no activity can handle the intent.
            }
            */
        }
    }

}