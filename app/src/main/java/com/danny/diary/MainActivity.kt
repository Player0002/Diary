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