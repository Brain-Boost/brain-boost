package com.example.opennewactivitybutton.ui.theme

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.opennewactivitybutton.database.entities.Alarm
import com.example.opennewactivitybutton.ringer.AlarmReceiver
import java.util.Calendar

class AlarmCreate(private val context: Context) {
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(alarm: Alarm) {
        if (alarm.status) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Add this line
                context.startActivity(intent)
                return
            }


            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, if (alarm.meridian == "AM" && alarm.hour == 12) 0 else if (alarm.meridian == "PM" && alarm.hour < 12) alarm.hour + 12 else alarm.hour)
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
            }

            // Schedule for the next day if the time has already passed
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }

            val requestCode = alarm.label.hashCode()
            val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("ALARM_LABEL", alarm.label)
            }.let { intent ->
                PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }
    }

    fun cancelAlarm(label: String) {
        val requestCode = label.hashCode()
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        alarmManager.cancel(alarmIntent)
    }
}
