package com.danny.diary.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Entity(tableName = "diary")
data class DiaryItem(
    @PrimaryKey val seq: String,
    val content: String,
    val time: Long
) {
    companion object {
        val LOADING = DiaryItem("", "", 0L)

        fun getInstance(date: Long, content: String): DiaryItem {
            val seq = Instant.ofEpochSecond(date).atZone(ZoneId.systemDefault()).format(
                DateTimeFormatter.ofPattern("yyyyMMdd")
            )
            return DiaryItem(seq, content, date)
        }
    }
}
