package com.colombia.credit.module.history

import com.colombia.credit.R
import com.colombia.credit.bean.resp.HistoryOrderInfo
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder

class HistoryAdapter(items: ArrayList<HistoryOrderInfo>) :
    BaseRecyclerViewAdapter<HistoryOrderInfo>(items, R.layout.layout_item_history) {

    override fun convert(holder: BaseViewHolder, item: HistoryOrderInfo, position: Int) {

    }
}