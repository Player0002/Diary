package com.danny.diary

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.danny.diary.adapter.DiaryAdapter
import com.danny.diary.databinding.ActivityMainBinding
import com.danny.diary.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
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

        floatingAction()
        observing()

        setContentView(binding.root)
    }

    private fun diaryRecycler() {
        binding.recycler.adapter = diaryAdapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
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