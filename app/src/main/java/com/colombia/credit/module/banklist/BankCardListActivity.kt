package com.colombia.credit.module.banklist

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspBankAccount
import com.colombia.credit.databinding.ActivityBankCardListBinding
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.maskBank
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SafeLinearLayoutManager
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.dp
import com.util.lib.hide
import com.util.lib.shape.ShapeImpl
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

// 银行账户列表页面
@AndroidEntryPoint
open class BankCardListActivity : BaseActivity() {

    protected val mBinding by binding<ActivityBankCardListBinding>()

    protected val mViewModel by lazyViewModel<BankCardViewModel>()

    protected var mCurrBankNo: String? = null

    protected val mAdapter: BankCardAdapter by lazy {
        BankCardAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)
        setViewModelLoading(mViewModel)

        val shape = ShapeImpl(this)
            .shapeCorners().radius(20.dp()).then()
            .shapeSolid().color(Color.TRANSPARENT).then()
            .shapeStroke().setStroke(1f.dp(), ContextCompat.getColor(this, R.color.color_dadada))
            .getShape()
        mBinding.bankFlAdd.background = shape

        mBinding.aivBack.setBlockingOnClickListener {
            finish()
        }
        mBinding.bankRecyclerview.layoutManager =
            SafeLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.bankRecyclerview.adapter = mAdapter
        mBinding.bankRecyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val item = mAdapter.getItemData<RspBankAccount.BankAccountInfo>(position)
                item?.changeSelector()
                mAdapter.setSelectorPosition(position)
                mCurrBankNo = item?.JJ41sQr
            }
        })

        mBinding.bankFlAdd.setBlockingOnClickListener {
            Launch.skipBankInfoAddActivityResult(this, 10)
        }

        mBinding.cardTvSave.setBlockingOnClickListener {
            // 调用接口， 保存信息后返回
            updateBank()
        }

        mViewModel.mUpdateLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                updataSuccess()
            } else {
                it.ShowErrorMsg(::updateBank)
            }
        }

        mViewModel.mBankAccountLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.oaeFxoW?.let { list ->
                    change(list.size == 5)
                    mAdapter.setItems(list)
                }
            } else it.ShowErrorMsg(::getBankAccount)
        }
        getBankAccount()
    }

    open fun updateBank() {
        mViewModel.updateBank(mAdapter.getSelectData()?.JJ41sQr.orEmpty(), "")
    }

    open fun updataSuccess() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            getBankAccount()
        }
    }

    private fun getBankAccount() {
        mViewModel.getBankAccountList()
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
            mBinding.bankFlAdd.hide()
        } else {
            mBinding.tvCardUp.hide()
            mBinding.bankFlAdd.show()
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
            holder.setText(R.id.tv_bank_no, maskBank(item.JJ41sQr))
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
            val preSelector = mSelectPosition
            if (preSelector > -1) {
                getItemData<RspBankAccount.BankAccountInfo>(preSelector)?.changeSelector()
                notifyItemChanged(preSelector)
            }
            mSelectPosition = position
            notifyItemChanged(position)
        }

        fun getSelectData() = getItemData<RspBankAccount.BankAccountInfo>(mSelectPosition)
    }
}