package com.example.myapplication.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.mp_barchart_stacked.StackedBarActivity

object NotificationManager: BaseNotificationManager() {

    const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"

    fun createNotificationChannel(context: Context) {
        createNotificationChannel(context, NOTIFICATION_CHANNEL_ID)
    }


    fun sendNotification(context: Context, message: String) {
        createNotificationChannel(context)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, StackedBarActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
            .addParentStack(StackedBarActivity::class.java)
            .addNextIntent(intent)
        val notificationPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                stackBuilder.getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            } else {
                stackBuilder.getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT)
            }


        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(getUniqueId(), notification)
    }

    private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())


}