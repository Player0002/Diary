package com.danny.diary

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0

object NotiUtil {
    fun NotificationManager.sendNotification(
        title: String,
        contents: String,
        context: Context
    ) {

        val content = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            content,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(Color.GREEN)
            .setContentTitle(title)
            .setContentText(contents)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notify(NOTIFICATION_ID, builder.build())
    }
}