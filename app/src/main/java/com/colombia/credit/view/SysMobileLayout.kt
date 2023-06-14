package com.colombia.credit.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutSysMobileBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.linearLayoutManager
import com.util.lib.dp

class SysMobileLayout : CardView {

    private val mBinding: LayoutSysMobileBinding =
        LayoutSysMobileBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context) : super(context) {
        initView(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        initView(context)
    }

    private val mAdapter by lazy {
        MobileAdapter(arrayListOf())
    }

    private var mClickResult: ClickResult? = null

    private fun initView(context: Context) {
        (mBinding.root as CardView).apply {
            setCardBackgroundColor(Color.WHITE)
            cardElevation = 14.dp()
        }
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter
        mBinding.recyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                mClickResult?.invoke(mAdapter.getItemData<String>(position).orEmpty())
            }
        })
    }

    fun setData(items: ArrayList<String>): SysMobileLayout {
        mAdapter.setItems(items)
        return this
    }

    fun setItemClick(click: ClickResult): SysMobileLayout {
        mClickResult = click
        return this
    }

    private class MobileAdapter(items: ArrayList<String>) :
        BaseRecyclerViewAdapter<String>(items, 0) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val tv = TextView(parent.context)
            tv.gravity = Gravity.CENTER_VERTICAL
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            tv.setBackgroundResource(R.drawable.selector_pop_item)
            tv.setTextColor(ContextCompat.getColor(parent.context, R.color.color_999999))
            val params = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 4f.dp()
            params.leftMargin = 10f.dp()
            params.bottomMargin = 4f.dp()
            tv.layoutParams = params
            return BaseViewHolder(tv)
        }

        override fun convert(holder: BaseViewHolder, item: String, position: Int) {
            (holder.itemView as TextView).text = item
        }
    }
}

typealias ClickResult = ((String) -> Unit)