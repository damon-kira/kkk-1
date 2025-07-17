package com.kira.ui.feature.explorer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kira.ui.core.extensions.replaceList
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.domain.model.FilesystemModel
import com.google.android.material.textview.MaterialTextView

class ServerAdapter(
    private val context: Context,
    private val addServer: () -> Unit,
) : BaseAdapter() {

    private val filesystemList = mutableListOf<FilesystemModel>()

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.item_filesystem, parent, false)
        val text = view.findViewById<MaterialTextView>(android.R.id.text1)
        val item = getItem(position)
        text?.text = item.title
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.item_dropdown, parent, false)
        val text = view.findViewById<MaterialTextView>(android.R.id.text1)
        val item = getItem(position)
        text?.text = item.title
        if (!isEnabled(position)) {
            view.setOnClickListener {
                addServer()
            }
        }
        return view
    }

    override fun areAllItemsEnabled() = false
    override fun isEnabled(position: Int): Boolean {
        return position < filesystemList.size - 1
    }

    override fun getItem(position: Int) = filesystemList[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getCount() = filesystemList.size

    fun submitList(filesystems: List<FilesystemModel>) {
        filesystemList.replaceList(filesystems)
        filesystemList.add(
            FilesystemModel(
                uuid = "null",
                title = context.getString(R.string.storage_add)
            )
        )
        notifyDataSetChanged()
    }
}