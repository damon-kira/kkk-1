package com.kira.ui.feature.explorer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.kira.ui.core.adapter.OnItemClickListener
import com.kira.ui.core.extensions.setSelectableBackground
import com.kira.ui.core.extensions.setSelectedBackground
import com.kira.ui.core.extensions.setTintAttr
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.data.utils.toReadableDate
import com.kira.ui.feature.explorer.data.utils.toReadableSize
import com.kira.ui.feature.explorer.databinding.ItemFileDetailedBinding
import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.FileType
import com.kira.ui.uikit.R as UiR
import com.google.android.material.R as MtrlR

class DetailedViewHolder(
    private val binding: ItemFileDetailedBinding,
    private val onItemClickListener: OnItemClickListener<FileModel>,
) : FileAdapter.FileViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup, onItemClickListener: OnItemClickListener<FileModel>): DetailedViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemFileDetailedBinding.inflate(inflater, parent, false)
            return DetailedViewHolder(binding, onItemClickListener)
        }
    }

    private lateinit var fileModel: FileModel

    init {
        itemView.setOnClickListener {
            onItemClickListener.onClick(fileModel)
        }
        itemView.setOnLongClickListener {
            onItemClickListener.onLongClick(fileModel)
        }
    }

    override fun bind(fileModel: FileModel, isSelected: Boolean) {
        this.fileModel = fileModel

        if (isSelected) {
            itemView.setSelectedBackground()
        } else {
            itemView.setSelectableBackground()
        }

        binding.itemTitle.text = fileModel.name
        binding.itemSubtitle.text = fileModel.lastModified.toReadableDate(
            pattern = itemView.context.getString(R.string.explorer_date_format),
        )
        binding.itemFileLength.text = fileModel.size.toReadableSize()

        binding.itemIcon.alpha = if (fileModel.isHidden) 0.45f else 1f
        binding.itemFileLength.isVisible = !fileModel.directory

        if (fileModel.directory) {
            binding.itemIcon.setImageResource(UiR.drawable.ic_folder)
            binding.itemIcon.setTintAttr(MtrlR.attr.colorPrimaryVariant)
        } else {
            binding.itemIcon.setTintAttr(MtrlR.attr.colorOnBackground)
            when (fileModel.type) {
                FileType.TEXT -> {
                    binding.itemIcon.setImageResource(UiR.drawable.ic_file_document)
                }
                FileType.ARCHIVE -> {
                    binding.itemIcon.setImageResource(UiR.drawable.ic_file_archive)
                    binding.itemIcon.setTintAttr(MtrlR.attr.colorPrimaryVariant)
                }
                FileType.IMAGE -> {
                    binding.itemIcon.setImageResource(UiR.drawable.ic_file_image)
                }
                FileType.AUDIO -> {
                    binding.itemIcon.setImageResource(UiR.drawable.ic_file_audio)
                }
                FileType.VIDEO -> {
                    binding.itemIcon.setImageResource(UiR.drawable.ic_file_video)
                }
                else -> {
                    binding.itemIcon.setImageResource(UiR.drawable.ic_file)
                }
            }
        }
    }
}