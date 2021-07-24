package com.danny.diary.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danny.diary.data.DiaryDao
import com.danny.diary.data.DiaryItem

@Database(entities = [DiaryItem::class], version = 1)
abstract class DiaryDb : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao
}