package com.colombia.credit.module.repay

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepayOrders
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.glide.GlideUtils

// 还款tab adapter
class RepayTabAdapter(
    items: ArrayList<RspRepayOrders.RepayOrderDetail>,
    private val recyclerView: RecyclerView
) :
    BaseRecyclerViewAdapter<RspRepayOrders.RepayOrderDetail>(
        items,
        R.layout.layout_item_repay_tab
    ) {

//    var bS6qpg4E: String? = null   //详情传这个
//    var W5KW6: String? = null      //计划id
//    var RA9GEePdNs: String? = null //产品log
//    var C2O8E6jjzd: String? = null //产品名字
//    var Eff0nA: Int = 0     //待还金额
//    var zbRV6Lg8jO: String? = null //还款日期
//    var gzBTFx: String? = null     //是否逾期
//    var q48Wml8N: String? = null   //1代表可以展期
//    var X32HrYq4u: String? = null  //展期金额
//    var QiZorG: String? = null     //1代表勾选中
//    var prr9Ie61: String? = null    //展期后时间
//    var GHMXDjtsUn: String? = null  //展期天数
//    var rCC18KSG: String? = null    //最初应还金额
//    var BPKD: String? = null // 产品id
//    var JKEAEEnOUZ: String? = null // 订单状态
//    var LvgWnBEX: String? = null //订单状态描述


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.getView<AppCompatImageView>(R.id.aiv_checkbox).setBlockingOnClickListener {
            val position = recyclerView.getChildLayoutPosition(holder.itemView)
            val itemData = getItemData<RspRepayOrders.RepayOrderDetail>(position)
            itemData?.changeSelect()
            notifyItemChanged(position)
            mSelectListener?.invoke()
        }
        holder.getView<TextView>(R.id.tv_extension).setBlockingOnClickListener {
            val position = recyclerView.getChildLayoutPosition(holder.itemView)
            getItemData<RspRepayOrders.RepayOrderDetail>(position)?.apply {
                mExtensionListener?.invoke(this)
            }
        }
        holder.getView<LinearLayout>(R.id.ll_item).setBlockingOnClickListener {
            val position = recyclerView.getChildLayoutPosition(holder.itemView)
            getItemData<RspRepayOrders.RepayOrderDetail>(position)?.apply {
                mOnItemClick?.invoke(this)
            }
        }
        return holder
    }

    override fun convert(
        holder: BaseViewHolder,
        item: RspRepayOrders.RepayOrderDetail,
        position: Int
    ) {
        val aivIcon = holder.getView<AppCompatImageView>(R.id.aiv_icon)
        GlideUtils.loadCornerImageFromUrl(
            holder.getContext(),
            item.RA9GEePdNs.orEmpty(),
            aivIcon,
            4f,
            R.drawable.svg_home_logo
        )
        holder.setText(R.id.tv_name, item.C2O8E6jjzd)
        holder.getView<TextView>(R.id.tv_status).apply {
            isSelected = !item.isOverdue()
            if (item.isOverdue()) {
                setText(R.string.tab_repay_overdue)
            } else {
                setText(R.string.tab_repay_normal)
            }
        }
        holder.getView<AppCompatImageView>(R.id.aiv_checkbox).isSelected = item.isCheck()

        holder.setText(R.id.tv_amount, getUnitString(item.Eff0nA.orEmpty()))
        holder.setText(R.id.tv_date, item.zbRV6Lg8jO.orEmpty())

        val selectItem = getSelectorItems()

        if (selectItem.isNotEmpty()) {
            mSelectListener?.invoke()
        }
    }

    fun getSelectorItems(): ArrayList<RspRepayOrders.RepayOrderDetail> {
        return currentItems.filter { it.isCheck() } as ArrayList<RspRepayOrders.RepayOrderDetail>
    }

    fun getTotalAmount(): Int {
        var total = 0
        getSelectorItems().forEach {
            total += it.Eff0nA?.toIntOrNull() ?: 0
        }
        return total
    }

    fun getSelectIds(): String {
        return getSelectorItems().map { it.W5KW6 }.joinToString(",")
    }

    override fun getItemId(position: Int): Long {
        return position * 1L
    }

    var mExtensionListener: ((RspRepayOrders.RepayOrderDetail) -> Unit)? = null

    var mSelectListener: (() -> Unit)? = null

    var mOnItemClick: ((RspRepayOrders.RepayOrderDetail) -> Unit)? = null
}