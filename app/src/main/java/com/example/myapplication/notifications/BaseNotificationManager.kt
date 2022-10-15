package com.example.myapplication.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

open class BaseNotificationManager {

    protected fun createNotificationChannel(context: Context, channelID: String, isMuted: Boolean = false) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(channelID) == null) {
                val notificationChannel = NotificationChannel(
                    channelID,
                    "MyApplication",
                    /*if (isMuted) NotificationManager.IMPORTANCE_LOW else */NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.setShowBadge(true)
//                notificationChannel.setSound(null, null)

                if (isMuted) {
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.BLUE
                    notificationChannel.enableVibration(false)
                }

                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }
}