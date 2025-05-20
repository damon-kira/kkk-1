package com.kira.learning.module.answer.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cache.lib.getContext
import com.kira.learning.R
import com.kira.learning.bean.QuestionProcessInfo
import com.kira.learning.module.adapter.BaseRecyclerViewAdapter
import com.kira.learning.module.adapter.BaseViewHolder
import com.kira.learning.popwindow.MsgListPopWindow
import androidx.core.graphics.drawable.toDrawable

class QuestionAdapter(
    items: ArrayList<QuestionProcessInfo.Step>,
    private val recyclerView: RecyclerView
) :
    BaseRecyclerViewAdapter<QuestionProcessInfo.Step>(
        items,
        R.layout.item_question
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
//        val showPopWindow = MsgListPopWindow(getContext())
        holder.getView<ImageView>(R.id.iv_process_tips).setOnClickListener {
            val position = recyclerView.getChildLayoutPosition(holder.itemView)
            Log.e("adapteradapter", "onCreateViewHolder: ${position}")
            val itemData = getItemData<QuestionProcessInfo.Step>(position)
//            showPopWindow.showPopWindow(it,"123")
//            showPopWindow
//            itemData?.changeSelect()
//            notifyItemChanged(position)
            showPopupWindow(it)

            mTipsListener?.invoke()
        }

        return holder
    }

    private fun showPopupWindow(anchorView: View) {
        // 1. 使用布局填充器加载PopupWindow的布局
        val popupView = LayoutInflater.from(anchorView.context).inflate(R.layout.pop_question_tips, null)

        // 2. 创建PopupWindow实例
        val popupWindow = PopupWindow(
            popupView,                      // 弹出窗口的视图
            ViewGroup.LayoutParams.WRAP_CONTENT,  // 宽度
            ViewGroup.LayoutParams.WRAP_CONTENT,  // 高度
            true                            // 是否可获取焦点
        ).apply {
            // 3. 设置背景（必须设置，否则点击外部无法关闭）
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            // 4. 设置点击外部可关闭
            isOutsideTouchable = true

            // 5. 设置消失监听器（可选）
            setOnDismissListener {
//                Toast.makeText(this@MainActivity, "PopupWindow已关闭", Toast.LENGTH_SHORT).show()
            }
        }

        // 6. 获取PopupWindow中的按钮并设置点击事件
        popupView.findViewById<Button>(R.id.popup_button).setOnClickListener {
            Toast.makeText(anchorView.context, "按钮被点击", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss() // 点击后关闭PopupWindow
        }

        // 7. 显示PopupWindow（相对于锚点视图下方显示）
        popupWindow.showAsDropDown(anchorView)

        // 替代显示方式：在屏幕中央显示
        // popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    override fun convert(
        holder: BaseViewHolder,
        item: QuestionProcessInfo.Step,
        position: Int
    ) {
        holder.setText(R.id.process_order,"${position + 1}.")
        holder.setText(R.id.process_tile, item.stepTitle)
        holder.setText(R.id.process_content, item.content)
    }

    override fun getItemId(position: Int): Long {
        return position * 1L
    }
    var mTipsListener: (() -> Unit)? = null
}