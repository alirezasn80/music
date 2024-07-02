package ir.flyap.madahi_new_1403.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.flyap.madahi_new_1403.R


class NotificationService : FirebaseMessagingService() {
    private val channelId = "channelId"
    private val channelName = "Audio"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val clickAction = remoteMessage.notification?.clickAction
        val url = remoteMessage.data["url"]
        val intent = Intent(clickAction)
        intent.putExtra("url", url)
        val pendingIntent = PendingIntent.getActivity(
            this,
            313,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_HIGH
        } else {
            NotificationCompat.PRIORITY_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Create a notification builder
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle((remoteMessage.notification?.title) ?: "")
            .setContentText((remoteMessage.notification?.body) ?: "")
            .setSmallIcon(R.drawable.ic_music)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()
        // Show the notification
        val notificationId = 1
        notificationManager.notify(notificationId, notification)
    }
}