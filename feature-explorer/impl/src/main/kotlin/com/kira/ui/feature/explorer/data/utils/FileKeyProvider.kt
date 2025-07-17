

package com.kira.ui.feature.explorer.data.utils

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.kira.ui.feature.explorer.ui.adapter.FileAdapter

class FileKeyProvider(
    private val recyclerView: RecyclerView,
) : ItemKeyProvider<String>(SCOPE_CACHED) {

    override fun getKey(position: Int): String {
        val adapter = recyclerView.adapter as FileAdapter
        return adapter.currentList[position].fileUri
    }

    override fun getPosition(key: String): Int {
        val adapter = recyclerView.adapter as FileAdapter
        return adapter.currentList.indexOfFirst { it.fileUri == key }
    }
}