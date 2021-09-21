package com.danny.diary.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pending =
                PendingIntent.getBroadcast(
                    context,
                    1,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val calNow = Calendar.getInstance()
            val calSet = calNow.clone() as Calendar

            calSet.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (calSet <= calNow) {
                calSet.add(Calendar.DATE, 1);
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calSet.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pending
            )
        }
    }
}