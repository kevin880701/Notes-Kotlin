package com.lhr.notes_kotlin.util.adapter

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lhr.notes_kotlin.AppConfig
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.ItemTextTitleBinding
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import java.io.File

class TextAdapter(val listener: Listener, activity: Activity): ListAdapter<NotesEntity, TextAdapter.ViewHolder>(LOCK_DIFF_UTIL) {
    var activity = activity
    companion object{
        val LOCK_DIFF_UTIL = object : DiffUtil.ItemCallback<NotesEntity>() {
            override fun areItemsTheSame(oldItem: NotesEntity, newItem: NotesEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: NotesEntity,
                newItem: NotesEntity
            ): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }

    interface Listener{
        fun onItemClick(item: NotesEntity)
        fun onItemLongClick(item: NotesEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTextTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemTextTitleBinding): RecyclerView.ViewHolder(binding.root){

        init {
            // bindingAdapterPosition無法使用，所以用adapterPosition替代
            binding.root.setOnClickListener {
                listener.onItemClick(getItem(adapterPosition))
            }

            binding.root.setOnLongClickListener {
                listener.onItemLongClick(getItem(adapterPosition))
                return@setOnLongClickListener true
            }
        }

        fun bind(notesEntity: NotesEntity){
            binding.textTitle.text = notesEntity.title
            if(notesEntity.image == 1){
                val folderPath = File(AppConfig.FILES_PATH, notesEntity.number)
                val imageFile = File(folderPath, "image.png")
                if (imageFile.exists()) {
                    // 需移除Tint，不然會覆蓋圖片
                    binding.image.clearColorFilter()
                    Glide.with(activity).load(imageFile).into(binding.image)
                }else{
                    // 設置TINT
                    binding.image.setColorFilter(
                        ContextCompat.getColor(activity, R.color.seed),
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
    }
}