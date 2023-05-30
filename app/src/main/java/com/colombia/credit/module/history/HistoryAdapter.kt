package com.colombia.credit.module.history

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspHistoryInfo
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.view.textview.EasyTextView
import com.common.lib.expand.setBlockingOnClickListener
import com.util.lib.invisible
import com.util.lib.show
import com.util.lib.span.SpannableImpl

class HistoryAdapter(items: ArrayList<RspHistoryInfo.HistoryOrderInfo>) :
    BaseRecyclerViewAdapter<RspHistoryInfo.HistoryOrderInfo>(items, R.layout.layout_item_history) {

    private val STATUS_REVIEW = "02" //审核中
    private val STATUS_VERIFI = "03" //审核通过
    private val STATUS_REFUSED = "04" //拒单
    private val STATUS_REPAY = "07" //未逾期
    private val STATUS_OVERDUE = "08" //已逾期
    private val STATUS_FAILURE = "09" //异常关闭，失效状态
    private val STATUS_SETTLE = "10" //已结清
    override fun convert(
        holder: BaseViewHolder,
        item: RspHistoryInfo.HistoryOrderInfo,
        position: Int
    ) {
//        var KxX0GIRzo: String? = null   // 订单id
//        var hlDgN: String? = null       // 订单状态
//        var pnFU: String? = null        // 待还款金额
//        var eeiu2lKWI: String? = null   // 借款金额
//        var tAnV: String? = null        // 待还期数
//        var mjMt2dTqSd: String? = null  // 订单期数
//        var lK97: String? = null        // 剩余天数
//        var npGPjAP: String? = null     // 产品名称
//        var PQw5: String? = null        // 产品logo
//        var znxlON0: String? = null     // 还款日期
//        var H0WVJP: String? = null      // 拒绝日期
//        var IIIn: String? = null        // 申请日期
//        var YlWUshbDy: String? = null   // 实际还款金额


        setItemStatus(holder, item)

        holder.setText(R.id.tv_name, item.npGPjAP)
        when (item.hlDgN) {
            STATUS_REVIEW, STATUS_VERIFI, STATUS_REPAY, STATUS_OVERDUE, STATUS_SETTLE -> {
                holder.setVisibility(R.id.ll_normal, true)
                holder.setVisibility(R.id.ll_failure, false)
                if (STATUS_REVIEW == item.hlDgN || item.hlDgN == STATUS_VERIFI) {
                    holder.setText(R.id.tv_amount, getUnitString(item.eeiu2lKWI.orEmpty())) // 借款金额
                    holder.setText(R.id.history_tv_date_text, R.string.history_review_date)
                    holder.setText(R.id.tv_date, item.IIIn.orEmpty()) // 到账日期
                } else {
                    holder.setText(R.id.tv_amount, getUnitString(item.pnFU.orEmpty())) // 还款金额
                    holder.setText(R.id.history_tv_date_text, R.string.history_repay_date)
                    holder.setText(R.id.tv_date, item.znxlON0.orEmpty()) // 还款日期
                }
            }
            else -> {
                holder.setVisibility(R.id.ll_normal, false)
                holder.setVisibility(R.id.ll_failure, true)
                if (item.hlDgN == STATUS_REFUSED) { // 拒绝
                    val textView = holder.getView<TextView>(R.id.tv_text_hint)
                    val refusedText = holder.getContext()
                        .getString(R.string.history_refused_text, item.H0WVJP.orEmpty())
                    textView.text = SpannableImpl().init(refusedText)
                        .color(
                            getColor(holder.getContext(), R.color.color_FE4F4F),
                            item.H0WVJP.orEmpty()
                        )
                        .getSpannable()
                    holder.setVisibility(R.id.history_tv_btn, false)
                } else if (item.hlDgN == STATUS_FAILURE) {// 失效，异常关闭
                    holder.setText(R.id.tv_text_hint, R.string.history_failure)
                    holder.getView<TextView>(R.id.history_tv_btn).apply {
                        show()
                        setBlockingOnClickListener {
                            mFailureListener?.invoke()
                        }
                    }
                }
            }
        }
    }

    private fun setItemStatus(holder: BaseViewHolder, item: RspHistoryInfo.HistoryOrderInfo) {
        val tvStatus = holder.getView<EasyTextView>(R.id.etv_status)
        val itemTvBtn = holder.getView<TextView>(R.id.tv_repay)
        val itemText = holder.getView<TextView>(R.id.tv_text)
        itemTvBtn.setBlockingOnClickListener {
            mRepayListener?.invoke(item.hlDgN == STATUS_REVIEW || item.hlDgN == STATUS_VERIFI, item)
        }
        when (item.hlDgN) {
            STATUS_REVIEW, STATUS_VERIFI -> { // 审核中
                tvStatus.solidColor = ContextCompat.getColor(
                    holder.getContext(),
                    R.color.color_FFF1DD
                )
                tvStatus.setTextColor(getColor(holder.getContext(), R.color.color_ff8200))
                tvStatus.setText(R.string.history_status_repay)
                itemText.setText(R.string.history_loan)
                itemTvBtn.setText(R.string.btn_review)
                itemTvBtn.setBackgroundColor(getColor(holder.getContext(), R.color.color_ff8200))
                itemTvBtn.show()
                itemTvBtn.isEnabled = true
            }
            STATUS_SETTLE -> { // 结清
                tvStatus.solidColor = ContextCompat.getColor(
                    holder.getContext(),
                    R.color.color_f0f0f0
                )
                tvStatus.setTextColor(getColor(holder.getContext(), R.color.color_666666))
                tvStatus.setText(R.string.history_status_settle)
                itemText.setText(R.string.repay_amount)
                itemTvBtn.invisible()
                itemTvBtn.isEnabled = false
            }
            STATUS_REPAY, STATUS_OVERDUE -> {
                tvStatus.solidColor = ContextCompat.getColor(
                    holder.getContext(),
                    R.color.color_D5FCDF
                )
                tvStatus.setTextColor(getColor(holder.getContext(), R.color.color_32C558))
                tvStatus.setText(R.string.history_status_review)
                itemText.setText(R.string.repay_amount)
                itemTvBtn.setBackgroundColor(getColor(holder.getContext(), R.color.colorPrimary))
                itemTvBtn.show()
                itemTvBtn.isEnabled = true
            }
            STATUS_REFUSED -> { //拒绝
                tvStatus.solidColor = ContextCompat.getColor(
                    holder.getContext(),
                    R.color.color_FDE0E0
                )
                tvStatus.setTextColor(getColor(holder.getContext(), R.color.color_fe4f4f))
                tvStatus.setText(R.string.history_status_refused)
                itemTvBtn.invisible()
                itemTvBtn.isEnabled = false
            }
            STATUS_FAILURE -> {
                itemTvBtn.isEnabled = false
                itemTvBtn.invisible()
                tvStatus.solidColor = ContextCompat.getColor(
                    holder.getContext(),
                    R.color.color_f0f0f0
                )
                tvStatus.setTextColor(getColor(holder.getContext(), R.color.color_666666))
                tvStatus.setText(R.string.history_status_failed)
            }
        }
    }

    var mFailureListener: (() -> Unit)? = null

    var mRepayListener: ((isReview: Boolean, RspHistoryInfo.HistoryOrderInfo) -> Unit)? = null
}