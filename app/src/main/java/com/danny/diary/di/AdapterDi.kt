package com.danny.diary.di

import com.danny.diary.adapter.DiaryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterDi {
    @Provides
    @Singleton
    fun provideAdapter(): DiaryAdapter {
        return DiaryAdapter()
    }
}