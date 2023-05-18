package com.colombia.credit.popwindow

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.util.lib.dp



class PopAdapter(
    dataList: List<PopData>,
    selectType: Int,
    var mItemPaddingLeft: Int
) : RecyclerView.Adapter<PopAdapter.PopHolder>() {

    companion object {
        const val TYPE_MOBILE = 100
    }

    var mDataList = dataList
    var mItemListener: onItemClickListener? = null
    var mCurrentType: Int = selectType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopHolder {
        val tv = TextView(parent.context)
        tv.gravity = Gravity.CENTER_VERTICAL
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        tv.setBackgroundResource(R.color.selector_pop_item)
        tv.setTextColor(ContextCompat.getColor(parent.context, R.color.color_999999))
        tv.setPadding(0, 0, 0, 8f.dp())
        val params = RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.topMargin = 8f.dp()
        params.leftMargin = 10f.dp()
        tv.layoutParams = params
        return PopHolder(tv)
    }

    override fun onBindViewHolder(holder: PopHolder, position: Int) {
        holder.mPopText?.run {
            text = mDataList[position].selectValues
            setPadding(mItemPaddingLeft, paddingTop, paddingRight, paddingBottom)
            isSelected = mDataList[position].isSelected
        }
        holder.mPopText?.setOnClickListener {
            for (element in mDataList) {
                element.isSelected = false
            }
            mDataList[position].isSelected = true
            mItemListener?.onItemClick(mDataList[position], mCurrentType, position + 1)//服务端是从1开始计
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setDataList(dataList: List<PopData>) {
        this.mDataList = dataList
        notifyDataSetChanged()
    }

    fun setCurrType(type: Int) {
        this.mCurrentType = type
    }

    fun setItemListener(listener: onItemClickListener) {
        mItemListener = listener
    }

    interface onItemClickListener {
        fun onItemClick(popData: PopData?, type: Int, position: Int)
    }

    class PopHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var mPopText: TextView? = null

        init {
            mPopText = itemview as TextView
        }
    }
}

