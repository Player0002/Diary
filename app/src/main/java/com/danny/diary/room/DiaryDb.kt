package com.danny.diary.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.danny.diary.data.DiaryDao

abstract class DiaryDb : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao

    companion object {
        var instance: DiaryDb? = null

        fun getDiaryInstance(applicationContext: Context): DiaryDb {
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(applicationContext, DiaryDb::class.java, "diary_db")
                    .fallbackToDestructiveMigration().build()
                instance!!
            }
        }
    }
}