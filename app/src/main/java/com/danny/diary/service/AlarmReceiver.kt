package com.danny.diary.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.danny.diary.NotiUtil.sendNotification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val manager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        manager.sendNotification("Diary", "일기를 작성할 시간이에요!", context)
    }
}