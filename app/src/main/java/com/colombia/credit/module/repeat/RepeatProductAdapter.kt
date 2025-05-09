package com.colombia.credit.module.repeat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.bean.resp.RepeatReview
import com.colombia.credit.bean.resp.RepeatWaitConfirmInfo
import com.colombia.credit.databinding.LayoutRepeatItemProductBinding
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.common.lib.glide.GlideUtils
import com.util.lib.dp
import com.util.lib.hide
import com.util.lib.show
import kotlin.math.min
import kotlin.math.roundToInt

class RepeatProductAdapter(items: ArrayList<RepeatProductInfo>, private val rv: RecyclerView) :
    BaseRecyclerViewAdapter<RepeatProductInfo>(items, R.layout.layout_repeat_product_item) {

    companion object {
        const val TYPE_WAIT = 10 // 等待确认订单
        const val TYPE_NORMAL = 11 // 默认产品列表
        const val TYPE_REVIEW = 12 // 审核中
        const val TYPE_EMPTY = 100
    }

    private var mWaitItems: ArrayList<RepeatWaitConfirmInfo> = arrayListOf()

    private var mRepeatReview: RepeatReview? = null

    private var mEmptyMaxHeight: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder = when (viewType) {
            TYPE_NORMAL -> {
                super.onCreateViewHolder(parent, viewType)
            }
            TYPE_EMPTY -> {
                BaseViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_no_product, parent, false)
                )
            }
            TYPE_REVIEW -> {
                BaseViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_item_repeat_review, parent, false)
                )
            }
            else -> {
                BaseViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_wait_order_item, parent, false)
                )
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_NORMAL -> {
                val finalPosi = getNormalItemPosition(position)
                convert(holder, currentItems[finalPosi], finalPosi)
            }
            TYPE_EMPTY -> {
                convertEmpty(holder, position)
            }
            TYPE_REVIEW -> {
                convertReview(holder, position)
            }
            else -> {
                convertWait(holder, getWaitItemPosition(position))
            }
        }
    }

    private fun convertReview(holder: BaseViewHolder, position: Int){
        val item = mRepeatReview ?:return
        holder.setText(R.id.tv_order, holder.getContext().getString(R.string.review_item_order_s, item.orders))
        holder.setText(R.id.tv_amount, getUnitString(item.amount))
        holder.setText(R.id.tv_date, item.date)
    }

    private fun convertWait(holder: BaseViewHolder, position: Int) {
        val items = mWaitItems
        if (position > items.size) return
        val item = items[position]
        holder.setText(R.id.tv_amount, getUnitString(item.yqGhrjOF2))

        val llItemLayout = holder.getView<LinearLayout>(R.id.ll_product)
        val products = item.I4Ai
        if (products.isNullOrEmpty()) {
            llItemLayout.removeAllViews()
            return
        }

        val productSize = products.size
        while (llItemLayout.childCount > productSize) {
            llItemLayout.removeViewAt(0)
        }
        // 最多添加4个
        products.take(4).reversed().forEachIndexed { index, waitConfirm ->
            val view = llItemLayout.getChildAt(index) ?: LayoutRepeatItemProductBinding.inflate(
                LayoutInflater.from(holder.getContext()),
                llItemLayout,
                false
            ).root
            val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f
            if (view.parent == null) {
                llItemLayout.addView(view, 0, layoutParams)
            }
            // 设置产品名称及icon
            val aivIcon = holder.getView<AppCompatImageView>(view, R.id.aiv_icon)
            GlideUtils.loadCornerImageFromUrl(
                holder.getContext(),
                waitConfirm.Gk9MGh.orEmpty(),
                aivIcon,
                4.dp(),
                R.drawable.ic_normal_image
            )
            val tvName = holder.getView<TextView>(view, R.id.tv_name)
            tvName.text = waitConfirm.S9ig78H
        }
        llItemLayout.requestLayout()
    }

    private fun convertEmpty(holder: BaseViewHolder, position: Int) {
        val scale = 1 - min(mWaitItems.size, 3) / 10f
        holder.getView<LinearLayout>(R.id.ll_empty).apply {
            updateLayoutParams<RecyclerView.LayoutParams> {
                height = (mEmptyMaxHeight * scale).roundToInt()
            }
            requestLayout()
            parent?.requestLayout()
        }
    }

    override fun convert(holder: BaseViewHolder, item: RepeatProductInfo, position: Int) {
        val img = holder.getView<AppCompatImageView>(R.id.aiv_image)
        GlideUtils.loadCornerImageFromUrl(
            holder.getContext(),
            item.Gk9MGh.orEmpty(),
            img,
            4.dp(),
            R.drawable.ic_normal_image
        )
        holder.setText(R.id.tv_appname, item.S9ig78H)
        holder.setText(
            R.id.tv_loan,
            "${getUnitString(formatCommon(item.sF1DFWU.orEmpty()))}~${formatCommon(item.g7tzi.orEmpty())}"
        )
        holder.setText(R.id.item_tv_interest_value, item.xXgaK4)
        holder.setText(R.id.item_tv_period_value, "${item.D9hR}~${item.cQ75eX5}")

        holder.getView<AppCompatImageView>(R.id.aiv_check).isSelected = item.RXYz == "1"

        holder.getView<TextView>(R.id.tv_tag).let {
            val tag = item.ir3MCCmbF3
            if (tag != "1" && tag != "2") {
                it.hide()
                return
            }
            it.show()
            it.isSelected = tag == "1"
            if (tag == "1") {
                it.setText(R.string.product_tag_risk)
            } else if (tag == "2"){
                it.setText(R.string.product_tag_safety)
            }
        }
    }

    fun setWaitItems(items: ArrayList<RepeatWaitConfirmInfo>?) {
        val preISEmpty = mWaitItems.isEmpty()
        mWaitItems.clear()
        items?.let {
            mWaitItems.addAll(it)
        }
        if (preISEmpty && mWaitItems.isEmpty()) return
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position < getReviewItemCount()) {
            return TYPE_REVIEW
        }
        if (position - getReviewItemCount() < getWaitItemCount()) {
            return TYPE_WAIT
        }
        if (position - getReviewItemCount() - getWaitItemCount() < currentItems.size) {
            return TYPE_NORMAL
        }
        return TYPE_EMPTY
    }

    override fun getItemCount(): Int {
        val waitSize = getWaitItemCount()
        val itemSize = currentItems.size
        val reviewSize = getReviewItemCount()
        if (itemSize > 0) {
            return waitSize + itemSize + reviewSize
        }
        return waitSize + 1 + reviewSize
    }

    fun getSelectorItems(): List<RepeatProductInfo> {
        return currentItems.filter { it.selector() }
    }

    fun setEmptyMaxHeight(height: Int) {
        this.mEmptyMaxHeight = height
        notifyDataSetChanged()
    }

    fun getWaitItemData(position: Int): RepeatWaitConfirmInfo? {
        if (position > mWaitItems.size || position < 0) return null
        return mWaitItems[position]
    }

    fun getWaitItemPosition(position: Int): Int {
        return position - getReviewItemCount()
    }

    fun getNormalItemPosition(position: Int): Int {
        return position - getReviewItemCount() - getWaitItemCount()
    }

    fun getNormalItemCount() = currentItems.size

    fun getWaitItemCount() = mWaitItems.size

    fun setRepeatReview(repeatReview: RepeatReview?) {
        this.mRepeatReview = repeatReview
    }

    private fun getReviewItemCount(): Int {
        return if (mRepeatReview == null) 0 else 1
    }
}