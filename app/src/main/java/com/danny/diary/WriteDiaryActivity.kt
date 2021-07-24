package com.danny.diary

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.danny.diary.data.DiaryItem
import com.danny.diary.databinding.ActivityWriteDiaryBinding
import com.danny.diary.viewmodel.WriteDiaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteDiaryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityWriteDiaryBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<WriteDiaryViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.todayDiary.observe(this) {
            if (it == DiaryItem.LOADING) {
                binding.contents.visibility = View.INVISIBLE
            } else {
                binding.contents.visibility = View.VISIBLE
                binding.progress.visibility = View.INVISIBLE
            }
        }

        binding.materialToolbar.setOnMenuItemClickListener {
            viewModel.saveDiary(binding.contents.text.toString())
            finish()
            false
        }

        setContentView(binding.root)
    }
}