package com.danny.diary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.danny.diary.data.DiaryItem
import com.danny.diary.databinding.DiaryItemBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DiaryAdapter : RecyclerView.Adapter<DiaryAdapter.DiaryHolder>() {

    private val differ = object : DiffUtil.ItemCallback<DiaryItem>() {
        override fun areItemsTheSame(oldItem: DiaryItem, newItem: DiaryItem): Boolean {
            return oldItem.seq == newItem.seq
        }

        override fun areContentsTheSame(oldItem: DiaryItem, newItem: DiaryItem): Boolean {
            return oldItem == newItem
        }

    }

    inner class DiaryHolder(private val binding: DiaryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(diaryItem: DiaryItem) {
            val dateInfo =
                Instant.ofEpochMilli(diaryItem.time).atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ofPattern("yyyy년 MM월 dd일의 일기")
                )
            binding.dateTime.text = dateInfo
            binding.root.setOnClickListener { onClickCallback?.invoke(diaryItem) }
        }
    }

    private var onClickCallback: ((DiaryItem) -> Unit)? = null
    fun setOnClickListener(listener: (DiaryItem) -> Unit) {
        onClickCallback = listener
    }

    val list = AsyncListDiffer(this, differ)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DiaryItemBinding.inflate(inflater, parent, false)
        return DiaryHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryHolder, position: Int) {
        holder.bind(list.currentList[position])
    }

    override fun getItemCount(): Int = list.currentList.size
}