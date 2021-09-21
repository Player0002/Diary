package com.danny.diary

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private val NOTIFICATION_ID = 0

object NotiUtil {
    fun NotificationManager.sendNotification(
        title: String,
        contents: String,
        context: Context
    ) {

        initNotificationChannel(context)

        val content = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            content,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, context.getString(R.string.app_name))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(Color.GREEN)
            .setContentTitle(title)
            .setContentText(contents)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notify(NOTIFICATION_ID, builder.build())
    }

    fun initNotificationChannel(context: Context) {
        val channelName = context.getString(R.string.app_name)
        val channelDesc = "Channel for notification"

        val channel =
            NotificationChannelCompat.Builder(
                channelName,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .apply {
                    setName(channelName)
                    setDescription(channelDesc)
                    setVibrationEnabled(true)
                }
        val compat = NotificationManagerCompat.from(context)

        compat.createNotificationChannel(channel.build())
    }

}