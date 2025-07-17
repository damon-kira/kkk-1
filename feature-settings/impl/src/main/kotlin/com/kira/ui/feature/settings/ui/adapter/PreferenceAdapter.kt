

package com.kira.ui.feature.settings.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kira.ui.core.adapter.OnItemClickListener
import com.kira.ui.core.extensions.getColorAttr
import com.kira.ui.core.extensions.setActivatedBackground
import com.kira.ui.core.extensions.setSelectableBackground
import com.kira.ui.feature.settings.databinding.ItemPreferenceBinding
import com.google.android.material.R as MtrlR

class PreferenceAdapter(
    private val onItemClickListener: OnItemClickListener<PreferenceHeader>,
) : ListAdapter<PreferenceHeader, PreferenceAdapter.PreferenceViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PreferenceHeader>() {
            override fun areItemsTheSame(oldItem: PreferenceHeader, newItem: PreferenceHeader): Boolean {
                return oldItem.screen == newItem.screen
            }
            override fun areContentsTheSame(oldItem: PreferenceHeader, newItem: PreferenceHeader): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceViewHolder {
        return PreferenceViewHolder.create(parent, onItemClickListener)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PreferenceViewHolder(
        private val binding: ItemPreferenceBinding,
        private val onItemClickListener: OnItemClickListener<PreferenceHeader>,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup, onItemClickListener: OnItemClickListener<PreferenceHeader>): PreferenceViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPreferenceBinding.inflate(inflater, parent, false)
                return PreferenceViewHolder(binding, onItemClickListener)
            }
        }

        private lateinit var preferenceHeader: PreferenceHeader

        init {
            itemView.setOnClickListener {
                onItemClickListener.onClick(preferenceHeader)
            }
        }

        fun bind(item: PreferenceHeader) {
            preferenceHeader = item
            binding.itemTitle.text = item.title
            binding.itemSubtitle.text = item.subtitle
            binding.root.isActivated = item.selected
            if (item.selected) {
                binding.itemTitle.setTextColor(
                    itemView.context.getColorAttr(MtrlR.attr.colorOnPrimary)
                )
                binding.itemSubtitle.setTextColor(
                    itemView.context.getColorAttr(MtrlR.attr.colorOnPrimary)
                )
                binding.root.setActivatedBackground()
            } else {
                binding.itemTitle.setTextColor(
                    itemView.context.getColorAttr(android.R.attr.textColorPrimary)
                )
                binding.itemSubtitle.setTextColor(
                    itemView.context.getColorAttr(android.R.attr.textColorSecondary)
                )
                binding.root.setSelectableBackground()
            }
        }
    }
}