

package com.kira.ui.feature.editor.ui.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kira.ui.feature.editor.databinding.ItemKeyboardKeyBinding
import com.kira.ui.feature.settings.domain.model.KeyModel
import com.common.kira.ui.dpToPx

class KeyAdapter(
    private val onKey: (KeyModel) -> Unit,
) : ListAdapter<KeyModel, KeyAdapter.KeyViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<KeyModel>() {
            override fun areItemsTheSame(oldItem: KeyModel, newItem: KeyModel): Boolean {
                return oldItem.value == newItem.value
            }
            override fun areContentsTheSame(oldItem: KeyModel, newItem: KeyModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyViewHolder {
        return KeyViewHolder.create(parent, onKey)
    }

    override fun onBindViewHolder(holder: KeyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class KeyViewHolder(
        private val binding: ItemKeyboardKeyBinding,
        private val onKey: (KeyModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup, onKey: (KeyModel) -> Unit): KeyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemKeyboardKeyBinding.inflate(inflater, parent, false)
                return KeyViewHolder(binding, onKey)
            }
        }

        private lateinit var keyModel: KeyModel

        init {
            itemView.setOnClickListener {
                onKey(keyModel)
            }
        }

        fun bind(item: KeyModel) {
            keyModel = item
            binding.title.text = item.display
            if (item.display.length > 1) {
                binding.title.updatePadding(bottom = 0)
                binding.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            } else {
                binding.title.updatePadding(bottom = 2.dpToPx())
                binding.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }
        }
    }
}