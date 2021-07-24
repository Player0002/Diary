package com.danny.diary.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.danny.diary.R
import com.danny.diary.data.DiaryDao
import com.danny.diary.room.DiaryDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDi {
    @Provides
    @Singleton
    fun provideDatabase(context: Application): DiaryDb {
        return Room.databaseBuilder(context, DiaryDb::class.java, "diary_db")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideService(database: DiaryDb): DiaryDao {
        return database.getDiaryDao()
    }

    @Provides
    @Singleton
    fun provideDefaultText(@ApplicationContext context: Context): String {
        return context.getString(R.string.default_txt)
    }
}