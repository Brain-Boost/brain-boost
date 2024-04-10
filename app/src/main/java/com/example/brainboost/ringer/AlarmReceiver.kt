package com.example.brainboost.ringer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.brainboost.Push
import com.example.brainboost.R
import com.example.brainboost.TicTacToe

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Context should not be null; if it is, we cannot proceed
        context?.let {

            Log.d("AlarmClock", "after ringtone push code complete...")

            Log.d("AlarmClock", "Alarm received!")
            val alarmRing = AlarmRing(it)
            Log.d("AlarmClock", "Before ringtone!")
            alarmRing.playRingtone()
            Log.d("AlarmClock", "After ringtone!")

            val pushNotificationHelper = Push(context)
            pushNotificationHelper.createNotificationChannel(
                channelId = "test_channel_id",
                channelName = "Test Channel",
                channelDescription = "This is a test notification channel"
            )

            // Prepare Intents
            val tttIntent = Intent(context, TicTacToe::class.java)

            pushNotificationHelper.createNotificationWithActions(
                channelId = "test_channel_id",
                notificationId = 1,
                title = "Test Notification",
                content = "This is a test notification with actions.",
                icon = R.drawable.ic_launcher_foreground,
                tttIntent = tttIntent,
                tttLabel = "Play TiC-Tac-Toe",
                tttIcon = R.drawable.ic_launcher_foreground
            )
        }
    }

}