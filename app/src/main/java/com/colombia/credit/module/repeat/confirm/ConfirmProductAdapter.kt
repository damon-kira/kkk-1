package com.colombia.credit.module.repeat.confirm

import androidx.appcompat.widget.AppCompatImageView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatConfirmProductInfo
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder

// 复贷确认额度 底部adapter
class ConfirmProductAdapter(items: ArrayList<RepeatConfirmProductInfo>) :
    BaseRecyclerViewAdapter<RepeatConfirmProductInfo>(items, R.layout.layout_repeat_confirm_item) {

    override fun convert(holder: BaseViewHolder, item: RepeatConfirmProductInfo, position: Int) {
        val aivCheck = holder.getView<AppCompatImageView>(R.id.aiv_check)
        aivCheck.isSelected = !aivCheck.isSelected
        item.isSelector = aivCheck.isSelected
    }

    fun getSelectorList() = run {
        val list = currentItems
        list.filter {
            it.isSelector
        }
        list
    }
}