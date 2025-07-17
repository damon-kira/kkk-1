package com.kira.ui.feature.explorer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kira.ui.core.adapter.TabAdapter
import com.kira.ui.feature.explorer.databinding.ItemTabDirectoryBinding
import com.kira.ui.filesystem.base.model.FileModel

class DirectoryAdapter : TabAdapter<FileModel, DirectoryAdapter.DirectoryViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<FileModel>() {
            override fun areItemsTheSame(oldItem: FileModel, newItem: FileModel): Boolean {
                return oldItem.fileUri == newItem.fileUri
            }
            override fun areContentsTheSame(oldItem: FileModel, newItem: FileModel): Boolean {
                return oldItem.fileUri == newItem.fileUri
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTabDirectoryBinding.inflate(inflater, parent, false)
        return DirectoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class DirectoryViewHolder(
        private val binding: ItemTabDirectoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    select(adapterPosition)
                }
            }
        }

        fun bind(item: FileModel) {
            binding.itemTitle.text = item.name
            updateSelected()
        }

        private fun updateSelected() {
            binding.selectionIndicator.isVisible = adapterPosition == selectedPosition
        }
    }
}