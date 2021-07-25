package com.danny.diary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.danny.diary.data.DiaryDao
import com.danny.diary.data.DiaryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(private val dao: DiaryDao) : ViewModel() {
    val diaries = liveData {
        dao.getAllDiary().collect { emit(it) }
    }

    fun removeDiary(diary: DiaryItem) = viewModelScope.launch {
        dao.deleteDiary(diary)
    }
}