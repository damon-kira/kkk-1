package com.colombia.credit.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.DictionaryInfo
import com.colombia.credit.databinding.DialogProcessSelectorBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.mapToPopData
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.MyDividerItemDecoration
import com.colombia.credit.module.adapter.linearLayoutManager
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.dp


class ProcessSelectorDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogProcessSelectorBinding>()

    private var mListener: ((DictionaryInfo) -> Unit)? = null

    private var mAdapter: BaseRecyclerViewAdapter<DictionaryInfo>? = null

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, WRAP, true)
        setCancelable(onTouchOut = false, cancel = true)
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.addItemDecoration(
            MyDividerItemDecoration(
                context,
                MyDividerItemDecoration.HORIZONTAL_LIST
            )
        )
        mAdapter = object :
            BaseRecyclerViewAdapter<DictionaryInfo>(arrayListOf(), 0) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val textview = TextView(parent.context).also {
                    val params = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    it.setPadding(0, 14f.dp(), 0, 14f.dp())
                    it.textSize = 13f
                    val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
                    val colors = intArrayOf(
                        getColor(parent.context, R.color.color_333333),
                        getColor(parent.context, R.color.color_999999)
                    )
                    it.setTextColor(ColorStateList(states, colors))
                    it.layoutParams = params
                    it.gravity = Gravity.CENTER_HORIZONTAL
                    it.setBackgroundColor(getColor(parent.context, R.color.color_F3F3F3))
                }

                return BaseViewHolder(textview)
            }

            override fun convert(holder: BaseViewHolder, item: DictionaryInfo, position: Int) {
                (holder.itemView as TextView).also {
                    it.text = item.value
                    it.isSelected = item.isSelected
                    if (item.isSelected) {
                        it.setBackgroundColor(getColor(holder.getContext(), R.color.color_F3F3F3))
                    } else {
                        it.setBackgroundColor(getColor(holder.getContext(), R.color.white))
                    }
                }
            }
        }
        mBinding.recyclerview.adapter = mAdapter
        mBinding.recyclerview.setOnItemClickListener(object :
            SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                mAdapter?.getItemData<DictionaryInfo>(position)?.let { item ->
                    dismiss()
                    mListener?.invoke(item)
                }
            }
        })
    }

    fun setDataAndTitle(
        title: String,
        mapData: MutableMap<String, String>,
        selectorTag: String
    ): ProcessSelectorDialog {
        mBinding.tvTitle.text = context.resources.getString(R.string.selector_s, title)
        val list = mapToPopData(mapData, selectorTag)
        mAdapter?.setItems(list)
        changeSize(list)
        return this
    }

    fun setOnClickListener(listener: (DictionaryInfo) -> Unit): ProcessSelectorDialog {
        this.mListener = listener
        return this
    }

    private fun changeSize(list: ArrayList<DictionaryInfo>) {
        //由于需要设置最大高度只有屏幕高度*0.6 所以需要判断一下最高能显示多少
        val d = window?.windowManager?.defaultDisplay?.height ?: 320 // 获取屏幕高度
        //recycleview的item每个高度是40dp ，recycleview顶部的高度一共是55dp
        if (list.size * 40.dp() + 55.dp() > d * 0.6) {
            setDisplaySize(MATCH, 0.7f, true)
            //设置一下recyclerview的高度
        } else {
            setDisplaySize(MATCH, WRAP, true)
        }
    }
}