package com.colombia.credit.module.repeat.confirm

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import com.colombia.credit.module.adapter.SpaceItemDecoration
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.module.firstconfirm.FirstConfirmViewModel
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.upload.UploadViewModel
import com.colombia.credit.util.AnimtorUtils
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.glide.GlideUtils
import com.common.lib.livedata.LiveDataBus
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

    private val mConfirmViewModel by lazyViewModel<FirstConfirmViewModel>()

    private val mInfoViewModel by lazyViewModel<RepeatConfirmViewModel>()

    private val mUploadViewModel by lazyViewModel<UploadViewModel>()

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
        setViewModelLoading(mConfirmViewModel)
        setViewModelLoading(mInfoViewModel)
        setAdapter()
        mBinding.aivArrow.setBlockingOnClickListener(this)
        mBinding.tvConfirm.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)

        mBinding.tvLoanList.text = getString(
            R.string.loan_success_data,
            time2Str(System.currentTimeMillis(), TimerUtil.REGEX_DDMMYYYY)
        )
        initObserver()
        getInfo()
    }

    private fun initObserver() {
        mConfirmViewModel.confirmLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
                Launch.skipApplySuccessActivity(this)
                finish()
            } else it.ShowErrorMsg(::confirm)
        }

        mInfoViewModel.mConfirmInfoLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@observerNonSticky
                mBankNo = data.KcGqvf.orEmpty()
                mBinding.tvBankNo.text = maskBank(data.KcGqvf)
                mBinding.tvRepayData.text = data.jcSrg9
                setText(data.bwAK6N3EuE.orEmpty(), data.FXE6B.orEmpty(), data.BM3HTNDY1b.orEmpty())
            } else {
                it.ShowErrorMsg(::getInfo)
            }
        }

        mInfoViewModel.mProListLiveData.observerNonSticky(this) {
            addHorizontalProduct(it)
            mAdapter.setItems(it)
        }
        LiveDataBus.getLiveData(BankEvent::class.java).observerNonSticky(this) {
            if (it.evnet == BankEvent.EVENT_BANK) {
                if (!it.params.isNullOrEmpty()) {
                    mBankNo = it.params.orEmpty()
                }
            }
        }
        mUploadViewModel.resultLiveData.observerNonSticky(this) {
            confirm()
        }
    }

    private fun setText(product: String, interest: String, loan: String) {
        mBinding.tvAmount.text = getUnitString(product)
        mBinding.tvTotalAmount.text = getUnitString(product)
        mBinding.tvAmountValue.text = getUnitString(loan)
        mBinding.tvTotalInterest.text = getUnitString(interest)
    }

    private fun getInfo() {
        mInfoViewModel.getConfirmInfo(productIds = mIds)

    }

    private var mRHeight: Int = 0

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.tv_confirm -> {
                mUploadViewModel.checkAndUpload()
            }
            R.id.aiv_arrow -> {
                // 底部list展开或收起
                mBinding.aivArrow.isEnabled = false
                mBinding.aivArrow.animate().rotationBy(180f).setDuration(550)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            mBinding.aivArrow.isEnabled = true
                        }
                    }).start()
                if (mRHeight == 0) {
                    mRHeight = mBinding.recyclerview.height
                }
                val start = if (mBinding.recyclerview.visibility == View.GONE) 0f else 1f
                val end = if (start == 0f) 1f else 0f
                AnimtorUtils.startAnima(mBinding.recyclerview, start, end, mRHeight, 550) {
                }
            }
            R.id.tv_bank_no -> {
                Launch.skipBankCardListActivity(
                    this,
                    mAdapter.getTotalAmount().toString(),
                    mIds,
                    mBankNo
                )
            }
        }
    }

    private fun confirm() {
        mConfirmViewModel.confirmLoan(mBankNo, mIds)
    }

    private fun setAdapter() {
        mBinding.recyclerview.linearLayoutManager()
        mBinding.recyclerview.adapter = mAdapter
        mBinding.recyclerview.itemAnimator?.changeDuration = 0
        mBinding.recyclerview.addItemDecoration(
            SpaceItemDecoration(
                SpaceItemDecoration.VERTICAL_LIST,
                8f.dp()
            )
        )
        mBinding.recyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                // 计算还款信息
                mAdapter.getItemData<RspRepeatCalcul.CalculDetail>(position)?.change()
                mAdapter.notifyItemChanged(position)
                val list = mAdapter.getSelectorList()
                var productAmount: Long = 0 // 产品金额
                var interest: Long = 0 // 总利息
                var loan = 0L // 到账金额
                list.forEach {
                    productAmount += (it.kDyJFWE?.toLongOrNull() ?: 0)
                    interest += (it.lceAYgef?.toLongOrNull() ?: 0)
                    loan += (it.UvS8UJEFy9?.toLongOrNull() ?: 0)
                }
                mIds = list.map { it.Bwh8vVa5wn }.joinToString(",")
                logger_d(TAG, "onItemClick: $productAmount,,,$interest,,,,$loan")
                setText(productAmount.toString(), interest.toString(), loan.toString())
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
            binding.tvName.text = product.um7clL0I
        }
        mBinding.llItem.requestLayout()
        mBinding.llProductList.requestLayout()
    }
}