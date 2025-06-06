package com.eas.pushnotificationdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            // Handle notification
            val title = it.title ?: "New Notification"
            val body = it.body ?: "New message"
            showNotification(title, body)
        }

        // Handle data message (if any)
        remoteMessage.data.entries.forEach {
            println("Key: ${it.key}, Value: ${it.value}")
            // Process the data
        }
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "your_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Your Channel Name",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your icon
            .setAutoCancel(true) // Dismiss the notification on touch
            .build()

        notificationManager.notify(1, notification) // Use a unique notification ID
    }
}