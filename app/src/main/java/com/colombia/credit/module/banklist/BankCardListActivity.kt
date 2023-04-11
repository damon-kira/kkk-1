package com.colombia.credit.module.banklist

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspBankAccount
import com.colombia.credit.databinding.ActivityBankCardListBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.maskString
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SafeLinearLayoutManager
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

// 银行账户列表页面
@AndroidEntryPoint
open class BankCardListActivity : BaseActivity() {

    protected val mBinding by binding<ActivityBankCardListBinding>()

    protected val mViewModel by lazyViewModel<BankCardViewModel>()

    protected val mAdapter: BankCardAdapter by lazy {
        BankCardAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.aivBack.setBlockingOnClickListener {
            finish()
        }
        mBinding.bankRecyclerview.layoutManager =
            SafeLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.bankRecyclerview.adapter = mAdapter
        mBinding.bankRecyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                mAdapter.getItemData<RspBankAccount.BankAccountInfo>(position)?.changeSelector()
                mAdapter.setSelectorPosition(position)
            }
        })

        mBinding.cardTvSave.setBlockingOnClickListener {
            // 调用接口， 保存信息后返回
        }

        mBinding.bankEtvAdd.setBlockingOnClickListener {
            Launch.skipBankInfoAddActivityResult(this, 10)
        }

        mViewModel.mBankAccountLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.oaeFxoW?.apply {
                    change(size == 5)
                    mAdapter.setItems(this)
                }
            } else it.ShowErrorMsg()
        }
        mViewModel.getBankAccountList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            mViewModel.getBankAccountList()
        }
    }

    //选择的银行
    protected fun getSelectBankInfo() = mAdapter.getSelectData()

    override fun onDestroy() {
        mBinding.bankRecyclerview.adapter = null
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

    class BankCardAdapter(items: ArrayList<RspBankAccount.BankAccountInfo>) :
        BaseRecyclerViewAdapter<RspBankAccount.BankAccountInfo>(
            items,
            R.layout.layout_bank_card_item
        ) {

        private var mSelectPosition: Int = -1

        override fun convert(
            holder: BaseViewHolder,
            item: RspBankAccount.BankAccountInfo,
            position: Int
        ) {
            holder.getView<TextView>(R.id.tv_name).let { tv ->
                tv.text = item.RPZ7
                tv.tag = item.oH3Jv
            }
            holder.setText(R.id.tv_bank_no, maskString(item.JJ41sQr, 4, 4))
            holder.getView<AppCompatImageView>(R.id.aiv_selector).isSelected = item.isSelector()
            if (item.isSelector()) {
                mSelectPosition = position
            }
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
            mSelectPosition = position
            notifyItemChanged(position)
        }

        fun getSelectData() = getItemData<RspBankAccount.BankAccountInfo>(mSelectPosition)
    }
}