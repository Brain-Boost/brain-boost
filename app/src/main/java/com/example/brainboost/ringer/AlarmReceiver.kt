package com.example.brainboost.ringer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.brainboost.Push
import com.example.brainboost.R
import com.example.brainboost.memoryGame.memoryFeature.presentation.MemoryGame
import com.example.brainboost.ui.theme.TicTacToeOpener
import com.example.brainboost.ui.theme.WurdleGame

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Context should not be null; if it is, we cannot proceed
        context?.let {

            Log.d("AlarmClock", "after ringtone push code complete...")

            Log.d("AlarmClock", "Alarm received!")
            val alarmRing = AlarmRing.getInstance(context)
            Log.d("AlarmClock", "Before ringtone!")
            alarmRing.playRingtone()
            Log.d("AlarmClock", "After ringtone!")

            val pushNotificationHelper = Push(context)
            pushNotificationHelper.createNotificationChannel(
                channelId = "test_channel_id",
                channelName = "Brain Boost Alarm",
                channelDescription = "This is channel is for notifying players of available games when Brain Boost alarms ring."
            )

            // Prepare Intents
            val tttIntent = Intent(context, TicTacToeOpener::class.java)
            val memIntent = Intent(context, MemoryGame::class.java)
            val wurdIntent = Intent(context, WurdleGame::class.java)

            pushNotificationHelper.createNotificationWithActions(
                channelId = "test_channel_id",
                notificationId = 1,
                title = "Brain Boost",
                content = "It's Brain Boost time! Win one of these game to shut off the alarm!",
                icon = R.drawable.ic_launcher_foreground,
                tttIntent = tttIntent,
                tttLabel = "Tic-Tac-Toe",
                tttIcon = R.drawable.ic_launcher_foreground,
                memIntent = memIntent,
                memLabel = "Memory Game",
                memIcon = R.drawable.ic_launcher_foreground,
                wurdIntent = wurdIntent,
                wurdLabel = "Wurdle",
                wurdIcon = R.drawable.ic_launcher_foreground
            )
        }
    }

}