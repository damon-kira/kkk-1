package com.kira.learning.module.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kira.learning.databinding.ItemChatBinding
import com.kira.learning.module.chat.bean.ChatEntity

class ChatAdapter : ListAdapter<ChatEntity, ChatAdapter.ChatViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding) 
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatEntity) {
            when {
                message.isUser -> {
                    binding.userMessage.text = message.message
                    binding.aiMessage.visibility = View.GONE
                    binding.userMessage.visibility = View.VISIBLE
                }
                else -> {
                    binding.aiMessage.text = message.message
                    binding.userMessage.visibility = View.GONE
                    binding.aiMessage.visibility = View.VISIBLE
                }
            }

            // 加载图片（如果有）
//            message.imageUrl?.let { imageUrl ->
//                Glide.with(binding.root.context)
//                    .load(imageUrl)
//                    .placeholder(R.drawable.ic_image_placeholder)
//                    .into(binding.messageImage)
//            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ChatEntity>() {
        override fun areItemsTheSame(
            oldItem: ChatEntity,
            newItem: ChatEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ChatEntity,
            newItem: ChatEntity
        ): Boolean = oldItem == newItem
    }
}