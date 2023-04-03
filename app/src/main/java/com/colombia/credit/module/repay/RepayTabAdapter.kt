package com.colombia.credit.module.repay

import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepayTabInfo
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder

// 还款tab adapter
class RepayTabAdapter(items: ArrayList<RspRepayTabInfo>) :
    BaseRecyclerViewAdapter<RspRepayTabInfo>(items, R.layout.layout_item_repay_tab) {

    override fun convert(holder: BaseViewHolder, item: RspRepayTabInfo, position: Int) {

    }

    fun getSelectorItems(): ArrayList<RspRepayTabInfo> {
        return currentItems.filter { it.isSelector } as ArrayList<RspRepayTabInfo>
    }

    fun getTotalAmount(): Int {
        var total = 0
        getSelectorItems().forEach {
            total += it.amount
        }
        return total
    }
}