

package com.kira.ui.feature.fonts.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kira.ui.core.extensions.createTypefaceFromPath
import com.kira.ui.feature.fonts.databinding.ItemFontBinding
import com.kira.ui.feature.fonts.domain.model.FontModel

class FontAdapter(
    private val actions: Actions,
) : ListAdapter<FontModel, FontAdapter.FontViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<FontModel>() {
            override fun areItemsTheSame(oldItem: FontModel, newItem: FontModel): Boolean {
                return oldItem.fontPath == newItem.fontPath
            }
            override fun areContentsTheSame(oldItem: FontModel, newItem: FontModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {
        return FontViewHolder.create(parent, actions)
    }

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FontViewHolder(
        private val binding: ItemFontBinding,
        private val actions: Actions,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup, actions: Actions): FontViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemFontBinding.inflate(inflater, parent, false)
                return FontViewHolder(binding, actions)
            }
        }

        private lateinit var fontModel: FontModel

        init {
            binding.actionSelect.setOnClickListener {
                actions.selectFont(fontModel)
            }
            binding.actionRemove.setOnClickListener {
                actions.removeFont(fontModel)
            }
        }

        fun bind(item: FontModel) {
            fontModel = item
            binding.itemTitle.text = item.fontName
            binding.itemContent.typeface = itemView.context.createTypefaceFromPath(item.fontPath)
            binding.actionRemove.isVisible = item.isExternal
        }
    }

    interface Actions {
        fun selectFont(fontModel: FontModel)
        fun removeFont(fontModel: FontModel)
    }
}