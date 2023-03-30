package com.colombia.credit.module.repeat

import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder

class RepeatProductAdapter(items: ArrayList<String>, layoutId: Int) :
    BaseRecyclerViewAdapter<String>(items, layoutId) {


    override fun convert(holder: BaseViewHolder, item: String, position: Int) {

    }
}