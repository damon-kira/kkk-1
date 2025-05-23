package com.kira.learning.module.home.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kira.learning.R
import com.kira.learning.bean.NavItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class NavAdapter(
    private val items: List<NavItem>,
    private val onItemClick: (NavItem) -> Unit
) : RecyclerView.Adapter<NavAdapter.ViewHolder>() {

    // 使用异步加载图标（假设图标需要网络加载）
    private val iconCache = LruCache<String, Bitmap>(10)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.iv_item_nav_icon)
        private val textView = view.findViewById<TextView>(R.id.tv_item_nav_text)

        // 优化：分离数据绑定逻辑
        fun bind(item: NavItem) {
            textView.text = item.title

            // 异步加载图标
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = iconCache.get(item.iconUrl) ?: loadIconFromNetwork(item.iconUrl.toString())
                bitmap?.let {
                    iconCache.put(item.iconUrl, it)
                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nav, parent, false)
        return ViewHolder(view).apply {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    private fun loadIconFromNetwork(url: String): Bitmap? {
        // 模拟网络加载
        return try {
            URL(url).openStream().use { input ->
                BitmapFactory.decodeStream(input)
            }
        } catch (e: Exception) {
            null
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return when {
//            items[position].isHeader -> TYPE_HEADER
//            else -> TYPE_ITEM
//        }
//    }

}