package com.colombia.credit.module.repeat.confirm

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepeatCalcul
import com.colombia.credit.databinding.ActivityRepeatConfirmBinding
import com.colombia.credit.databinding.LayoutRepeatItemProductBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.module.firstconfirm.FirstConfirmViewModel
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.glide.GlideUtils
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.TimerUtil
import com.util.lib.dp
import com.util.lib.log.logger_d
import com.util.lib.time2Str
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepeatConfirmActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_IDS = "key_ids"
    }

    private val mBinding by binding<ActivityRepeatConfirmBinding>()

    private val mAdapter by lazy {
        ConfirmProductAdapter(arrayListOf())
    }

    private val mConfirmLoanViewModel by lazyViewModel<FirstConfirmViewModel>()

    private val mInfoViewModel by lazyViewModel<RepeatConfirmViewModel>()

    private var mTotalAmount = ""
    private var mIds = ""
    private var mBankNo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)
        mIds = intent.getStringExtra(EXTRA_IDS).orEmpty()
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }
        setAdapter()
        mBinding.aivArrow.setBlockingOnClickListener(this)
        mBinding.tvConfirm.setBlockingOnClickListener(this)
        mBinding.tvCancel.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)

        mBinding.tvLoanList.text = getString(R.string.loan_success_data, time2Str(System.currentTimeMillis(), TimerUtil.REGEX_DDMMYYYY))
        mBinding.recyclerview.itemAnimator?.changeDuration = 0

        mInfoViewModel.mConfirmInfoLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@observerNonSticky
                mBinding.tvAmount.text = getUnitString(data.bwAK6N3EuE)
                mBinding.tvBankNo.text = maskString(data.KcGqvf, 3, 3)
                mBinding.tvAmountValue.text = getUnitString(data.BM3HTNDY1b)
                mBinding.tvTotalAmount.text = getUnitString(data.bwAK6N3EuE)
                mBinding.tvTotalInterest.text = getUnitString(data.FXE6B)
                mBinding.tvRepayData.text = data.jcSrg9
            } else {
                it.ShowErrorMsg(::getInfo)
            }
        }

        mInfoViewModel.mProListLiveData.observerNonSticky(this) {
            addHorizontalProduct(it)
            mAdapter.setItems(it)
        }

        logger_d(TAG, "ids = $mIds")
        getInfo()
    }

    private fun getInfo() {
        mInfoViewModel.getConfirmInfo(productIds = mIds)
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.tv_confirm -> {

            }
            R.id.tv_cancel -> {

            }
            R.id.aiv_arrow -> {
                // 底部list展开或收起
            }
            R.id.tv_bank_no -> {
                Launch.skipBankCardListActivity(this, mTotalAmount, mIds, mBankNo)
            }
        }
    }

    private fun setAdapter() {
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter
        mBinding.recyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                // 计算还款信息
                mAdapter.notifyItemChanged(position)
            }
        })
    }

    private fun addHorizontalProduct(products: ArrayList<RspRepeatCalcul.CalculDetail>) {
        // 最多添加4个
        products.take(4).reversed().forEach { product ->
            val binding = LayoutRepeatItemProductBinding.inflate(
                LayoutInflater.from(this),
                mBinding.llProductList,
                false
            )
            val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f
            mBinding.llProductList.addView(binding.root, 0, layoutParams)
            // 设置产品名称及icon
            GlideUtils.loadCornerImageFromUrl(
                this,
                product.rOVhdGR.orEmpty(),
                binding.aivIcon,
                4.dp(),
                R.drawable.svg_home_logo
            )
        }
    }

}