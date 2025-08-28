package com.kira.learning.module.ai

import android.text.Spannable
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kira.learning.R
import com.kira.learning.module.adapter.BaseRecyclerViewAdapter
import com.kira.learning.module.adapter.BaseViewHolder
import com.kira.learning.bean.dao.ChatMessage
import io.noties.markwon.Markwon

class AIChatAdapter(
    items: ArrayList<ChatMessage>,
    private val markwon: Markwon,
    var ChatMode: String,
    private val recyclerView: RecyclerView
) :
    BaseRecyclerViewAdapter<ChatMessage>(
        items,
        R.layout.item_message
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)


//        holder.getView<AppCompatImageView>(R.id.aiv_checkbox).setBlockingOnClickListener {
//            val position = recyclerView.getChildLayoutPosition(holder.itemView)
//            val itemData = getItemData<ChatMessage>(position)
//            itemData?.changeSelect()
//            notifyItemChanged(position)
//            mSelectListener?.invoke()
//        }
//        holder.getView<TextView>(R.id.tv_extension).setBlockingOnClickListener {
//            val position = recyclerView.getChildLayoutPosition(holder.itemView)
//            getItemData<ChatMessage>(position)?.apply {
//                mExtensionListener?.invoke(this)
//            }
//        }
//        holder.getView<LinearLayout>(R.id.ll_item).setBlockingOnClickListener {
//            val position = recyclerView.getChildLayoutPosition(holder.itemView)
//            getItemData<ChatMessage>(position)?.apply {
//                mOnItemClick?.invoke(this, position)
//            }
//        }

        return holder
    }

    override fun convert(
        holder: BaseViewHolder,
        item: ChatMessage,
        position: Int
    ) {
        val markdownText = item.content // 假设getMessage()返回Markdown文本
        // 使用Markwon渲染Markdown
        val spannable = markwon.toMarkdown(markdownText) as Spannable
        if (ChatMode == "english") {
            holder.setImageResource(R.id.bot_icon, R.drawable.teacher)
            holder.setText(R.id.bot_name, "kira-english")
        } else {
            holder.setImageResource(R.id.bot_icon, R.drawable.ic_kira_logo)
            holder.setText(R.id.bot_name, "kira-teacher")
        }
        holder.setVisibility(R.id.user_icon, "USER" == item.isUser)
        holder.setVisibility(R.id.user_name, "USER" == item.isUser)
        holder.setVisibility(R.id.messageSend, "USER" == item.isUser)
        holder.setVisibility(R.id.bot_icon, "USER" != item.isUser)
        holder.setVisibility(R.id.bot_name, "USER" != item.isUser)
        holder.setVisibility(
            R.id.messageReceive,
            "USER" != item.isUser && spannable.toString().isNotEmpty()
        )
        holder.setText(R.id.messageSend, spannable.toString())
        holder.setText(R.id.messageReceive, spannable.toString())
        holder.setVisibility(
            R.id.loading_wait,
            "USER" != item.isUser && spannable.toString().isEmpty()
        )

    }

//    fun getSelectorItems(): ArrayList<ChatMessage> {
//        return currentItems.filter { it.isCheck() } as ArrayList<ChatMessage>
//    }

    override fun getItemId(position: Int): Long {
        return position * 1L
    }

    var mExtensionListener: ((ChatMessage) -> Unit)? = null

    var mSelectListener: (() -> Unit)? = null

    var mOnItemClick: ((ChatMessage, position: Int) -> Unit)? = null
}