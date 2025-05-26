package rahul.lohra.networkmonitor.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NetworkLogNotifier {

    private const val CHANNEL_ID = "network_log_channel"
    private const val NOTIFICATION_ID = 769
    private const val CHANNEL_NAME = "Network Logs"
    private const val CONTENT_TITLE = "Recording Network activity"

    fun setup(context: Context) {
        initChannel(context)
        NotificationSdkInitializer.initialize(context)
    }

    internal fun initChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Displays live network logs for debugging"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, logSummary: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(CONTENT_TITLE)
            .setContentText(logSummary)
            .setStyle(NotificationCompat.BigTextStyle().bigText(logSummary))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(getPendingIntent(context))
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("NetworkLogNotifier", "No permission to push Notifications")
            return
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, Class.forName("rahul.lohra.networkmonitor.presentation.ui.NetworkMonitorActivity")).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Use FLAG_IMMUTABLE for Android 12+
        )
    }
}
