import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.opennewactivitybutton.R
import com.example.opennewactivitybutton.games.Stub

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

    fun showAlarmNotification() {
        val intent = Intent(context, Stub::class.java).apply { // takes you to a game
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm) // Set your own icon
            .setContentTitle("Alarm!")
            .setContentText("Tap to open the app.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "alarm_channel_id"
        private const val CHANNEL_NAME = "Alarm Notification"
        private const val CHANNEL_DESCRIPTION = "Notifies when the alarm goes off"
        private const val NOTIFICATION_ID = 1
    }
}
