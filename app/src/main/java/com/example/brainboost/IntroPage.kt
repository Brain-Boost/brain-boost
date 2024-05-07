package com.example.brainboost

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class IntroPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
                navigateToMain()
                return
            }
        }

        setContent {
            IntroScreen()
        }
    }

    @Composable
    fun IntroScreen() {
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Hello new user, welcome to Brain Boost! To get started, we'll need just a couple of permissions first.")
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                requestAlarmPermission(context)
            }) {
                Text("Enable Alarm Permission")
            }
        }
    }

    private fun requestAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("Permission", "Entered Intro request")
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, HomePage::class.java))
        finish()
    }

}