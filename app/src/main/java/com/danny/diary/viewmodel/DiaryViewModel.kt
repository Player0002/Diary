package com.danny.diary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.danny.diary.data.DiaryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(private val dao: DiaryDao) : ViewModel() {
    val diaries = liveData {
        dao.getAllDiary().collect { emit(it) }
    }
}