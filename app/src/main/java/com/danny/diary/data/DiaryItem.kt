package com.danny.diary.data

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(tableName = "diary")
data class DiaryItem(
    @PrimaryKey val seq: Int,
    val content: String,
    val time: Long
) : Serializable {
    companion object {
        val LOADING = DiaryItem(0, "", 0L)

        fun getInstance(date: Long, content: String): DiaryItem {
            val seq = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).format(
                DateTimeFormatter.ofPattern("yyyyMMdd")
            ).toInt()
            Log.d("LOGA", "$seq")
            return DiaryItem(seq, content, date)
        }
    }
}
