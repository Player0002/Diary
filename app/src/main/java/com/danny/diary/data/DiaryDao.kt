package com.danny.diary.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diaryItem: DiaryItem)

    @Delete
    suspend fun deleteDiary(diaryItem: DiaryItem)

    @Query("select * from diary")
    fun getAllDiary(): Flow<List<DiaryItem>>

    @Query("select * from diary where seq = :seq")
    suspend fun getDiary(seq: String): DiaryItem
}