package com.example.brainboost

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import com.example.brainboost.HomePage

class IntroPage : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissionsAndNavigate()
    }

    override fun onResume() {
        super.onResume()
        checkPermissionsAndNavigate()
    }

    private fun checkPermissionsAndNavigate() {
        if (allPermissionsGranted()) {
            Toast.makeText(this, "All permissions enabled!", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            showPermissionScreen()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return hasAlarmPermission() && hasNotificationPermission()
    }

    private fun hasAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(AlarmManager::class.java).canScheduleExactAlarms()
        } else true // Assume permission is granted on API levels below S
    }

    private fun hasNotificationPermission(): Boolean {
        return NotificationManagerCompat.from(this).areNotificationsEnabled()
    }

    private fun showPermissionScreen() {
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
            Text("To get started, we'll need to enable a couple of permissions first.", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { requestAlarmPermission(context) }) {
                Text("Enable Alarm Permission", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { requestNotificationPermission(context) }) {
                Text("Enable Notification Permission", fontWeight = FontWeight.Bold)
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, HomePage::class.java))
        finish()
    }

    private fun requestAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }
    }

    private fun requestNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }
    }
}
