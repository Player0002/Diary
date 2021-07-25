package com.danny.diary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.danny.diary.data.DiaryDao
import com.danny.diary.data.DiaryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WriteDiaryViewModel @Inject constructor(
    private val dao: DiaryDao,
    private val defaultStr: String
) : ViewModel() {
    private val today = Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).format(
        DateTimeFormatter.ofPattern("yyyyMMdd")
    ).toInt()
    val todayDiary = liveData {
        emit(DiaryItem.LOADING)
        Log.d("LOGA", "$today")
        emit(
            dao.getDiary(today) ?: DiaryItem.getInstance(
                Calendar.getInstance().timeInMillis,
                defaultStr
            )
        )
    }

    fun saveDiary(content: String) = viewModelScope.launch {
        val diaryItem = DiaryItem.getInstance(Calendar.getInstance().timeInMillis, content)
        dao.insertDiary(diaryItem)
    }

}