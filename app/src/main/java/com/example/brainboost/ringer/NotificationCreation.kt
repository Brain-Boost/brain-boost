
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationCreation(private val context: Context) {

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        private const val CHANNEL_ID = "alarm_channel_id"
        private const val CHANNEL_NAME = "Alarm Notification"
        private const val CHANNEL_DESCRIPTION = "Notifies when the alarm goes off"
        private const val NOTIFICATION_ID = 1
    }
}
