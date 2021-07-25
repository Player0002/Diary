package com.danny.diary

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.diary.adapter.DiaryAdapter
import com.danny.diary.databinding.ActivityMainBinding
import com.danny.diary.service.AlarmReceiver
import com.danny.diary.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DiaryViewModel>()

    @Inject
    lateinit var diaryAdapter: DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        diaryRecycler()

        alarm()

        floatingAction()
        observing()

        swiping()

        createChannel(
            "diary_channel",
            "Diary"
        )
        setContentView(binding.root)
    }

    private fun swiping() {
        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT))) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removeDiary(diaryAdapter.list.currentList[viewHolder.adapterPosition])
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recycler)
    }

    private fun alarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar: Calendar = (Calendar.getInstance().clone() as Calendar).apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pending
        )
    }

    private fun diaryRecycler() {
        binding.recycler.adapter = diaryAdapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        diaryAdapter.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    DiaryViewerActivity::class.java
                ).putExtra(DiaryViewerActivity.DIARY_ITEM, it)
            )
        }
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    private fun observing() {
        viewModel.diaries.observe(this) {
            diaryAdapter.list.submitList(it)
        }
    }

    private fun floatingAction() {
        binding.floating.setOnClickListener {
            startActivity(Intent(this, WriteDiaryActivity::class.java))
        }
    }
}