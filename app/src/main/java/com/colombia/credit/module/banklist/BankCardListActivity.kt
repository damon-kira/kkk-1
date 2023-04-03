package com.colombia.credit.module.banklist

import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.BankCardInfo
import com.colombia.credit.databinding.ActivityBankCardListBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.formatCommon
import com.colombia.credit.expand.maskString
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SafeLinearLayoutManager
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

// 银行账户列表页面
@AndroidEntryPoint
open class BankCardListActivity : BaseActivity() {

    companion object {
        const val EXTRA_AMOUNT = "key_amount"
    }

    protected val mBinding by binding<ActivityBankCardListBinding>()

    protected val mViewModel by lazyViewModel<BankCardViewModel>()

    private val mAdapter: BankCardAdapter by lazy {
        BankCardAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        var amount = intent.getStringExtra(EXTRA_AMOUNT).orEmpty()
        amount = getString(R.string.amount_unit, formatCommon(amount))
        mBinding.bankCardTvApply.text = getString(R.string.bank_card_btn_text, amount)

        mBinding.aivBack.setBlockingOnClickListener {
            finish()
        }
        val list = arrayListOf<BankCardInfo>()
        for (index in 0..3) {
            val info = BankCardInfo().also {
                it.bankName = "bank$index"
                it.bankNo = "38403849384$index"
            }
            list.add(info)
        }

        change(list.size == 5)

        mBinding.bankRecyclerview.layoutManager =
            SafeLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.bankRecyclerview.adapter = mAdapter
        mBinding.bankRecyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                mAdapter.setSelectorPosition(position)
            }
        })
        mAdapter.setItems(list)

        mBinding.cardTvSave.setBlockingOnClickListener {
            // 调用接口， 保存信息后返回
        }
    }

    override fun onDestroy() {
        mBinding.bankRecyclerview.adapter = null
        mBinding.bankCardTvApply.removeCallbacks {}
        super.onDestroy()
    }

    private fun change(isCardUp: Boolean) {
        if (isCardUp) {
            mBinding.tvCardUp.show()
            mBinding.bankEtvAdd.hide()
        } else {
            mBinding.tvCardUp.hide()
            mBinding.bankEtvAdd.show()
        }
    }

    class BankCardAdapter(items: ArrayList<BankCardInfo>) :
        BaseRecyclerViewAdapter<BankCardInfo>(items, R.layout.layout_bank_card_item) {

        private var mSelector: Int = -1

        override fun convert(holder: BaseViewHolder, item: BankCardInfo, position: Int) {
            holder.setText(R.id.tv_name, item.bankName)
            holder.setText(R.id.tv_bank_no, maskString(item.bankNo, 4, 4))
            holder.getView<AppCompatImageView>(R.id.aiv_selector).isSelected = mSelector == position
            when (position) {
                0 -> {
                    holder.getView<ConstraintLayout>(R.id.cl_layout)
                        .setBackgroundResource(R.drawable.image_bank_card_bg1)
                    holder.getView<AppCompatImageView>(R.id.aiv_icon)
                        .setImageResource(R.drawable.svg_bank_icon1)
                }
                1 -> {
                    holder.getView<ConstraintLayout>(R.id.cl_layout)
                        .setBackgroundResource(R.drawable.image_bank_card_bg2)
                    holder.getView<AppCompatImageView>(R.id.aiv_icon)
                        .setImageResource(R.drawable.svg_bank_icon2)
                }
                2 -> {
                    holder.getView<ConstraintLayout>(R.id.cl_layout)
                        .setBackgroundResource(R.drawable.image_bank_card_bg3)
                    holder.getView<AppCompatImageView>(R.id.aiv_icon)
                        .setImageResource(R.drawable.svg_bank_icon3)
                }
                3 -> {
                    holder.getView<ConstraintLayout>(R.id.cl_layout)
                        .setBackgroundResource(R.drawable.image_bank_card_bg4)
                    holder.getView<AppCompatImageView>(R.id.aiv_icon)
                        .setImageResource(R.drawable.svg_bank_icon4)
                }
                4 -> {
                    holder.getView<ConstraintLayout>(R.id.cl_layout)
                        .setBackgroundResource(R.drawable.image_bank_card_bg5)
                    holder.getView<AppCompatImageView>(R.id.aiv_icon)
                        .setImageResource(R.drawable.svg_bank_icon5)
                }
            }
        }

        fun setSelectorPosition(position: Int) {
            mSelector = position
            notifyItemChanged(position)
        }
    }
}