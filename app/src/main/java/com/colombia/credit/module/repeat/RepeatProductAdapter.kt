package com.colombia.credit.module.repeat

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.common.lib.glide.GlideUtils
import com.util.lib.dp

class RepeatProductAdapter(items: ArrayList<RepeatProductInfo>) :
    BaseRecyclerViewAdapter<RepeatProductInfo>(items, R.layout.layout_repeat_product_item) {

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
            it.isSelected = item.ir3MCCmbF3 == "1"
            if (item.ir3MCCmbF3 == "1") {
                it.setText(R.string.product_tag_risk)
            } else {
                it.setText(R.string.product_tag_safety)
            }
        }
    }

    fun getSelectorItems(): List<RepeatProductInfo> {
        return currentItems.filter { it.selector() }
    }
}