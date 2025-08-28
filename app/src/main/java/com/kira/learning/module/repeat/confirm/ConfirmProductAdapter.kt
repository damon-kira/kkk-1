package com.kira.learning.module.repeat.confirm

import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kira.learning.R
import com.kira.learning.bean.res.RspRepeatCalcul
import com.kira.learning.expand.getUnitString
import com.kira.learning.module.adapter.BaseRecyclerViewAdapter
import com.kira.learning.module.adapter.BaseViewHolder
import com.common.lib.glide.GlideUtils
import com.util.lib.dp

// 复盘确认额度 底部adapter
class ConfirmProductAdapter(items: ArrayList<RspRepeatCalcul.CalculDetail>) :
    BaseRecyclerViewAdapter<RspRepeatCalcul.CalculDetail>(
        items,
        R.layout.layout_repeat_confirm_item
    ) {

    override fun convert(
        holder: BaseViewHolder,
        item: RspRepeatCalcul.CalculDetail,
        position: Int
    ) {
        val aivCheck = holder.getView<AppCompatImageView>(R.id.aiv_check)
        aivCheck.isSelected = item.isCheck == 1
        holder.getView<ConstraintLayout>(R.id.cl_item).isSelected = aivCheck.isSelected

        val iconView = holder.getView<AppCompatImageView>(R.id.aiv_product_icon)
        GlideUtils.loadCornerImageFromUrl(
            holder.getContext(),
            item.rOVhdGR.orEmpty(),
            iconView,
            4.dp(),
            R.drawable.ic_normal_image
        )
        holder.setText(R.id.tv_product_name, item.um7clL0I)
        holder.setText(R.id.tv_product_amount, getUnitString(item.kDyJFWE))
        holder.setText(
            R.id.tv_product_interest,
            holder.getContext().getString(R.string.product_interest, getUnitString(item.lceAYgef))
        )
    }

    fun getSelectorList() = run {
        val list = currentItems
        list.filter {
            it.isCheck == 1
        }
    }

    fun getTotalAmount() = run {
        val list = currentItems
        var result = 0L
        list.filter {
            it.isCheck == 1
        }.map { it.kDyJFWE?.toLongOrNull() ?: 0 }
            .forEach {
                result += it
            }
        result
    }
}