package com.colombia.credit.module.repeat.confirm

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bigdata.lib.loanPageStayTime
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RspRepeatCalcul
import com.colombia.credit.databinding.ActivityRepeatConfirmBinding
import com.colombia.credit.databinding.LayoutRepeatItemProductBinding
import com.colombia.credit.dialog.CancelAutoHintDialog
import com.colombia.credit.dialog.UploadDialog
import com.colombia.credit.expand.*
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.module.adapter.SpaceItemDecoration
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.module.firstconfirm.AutoConfirmViewModel
import com.colombia.credit.module.firstconfirm.FirstConfirmViewModel
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.upload.UploadViewModel
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.appPermissions
import com.colombia.credit.util.AnimtorUtils
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.glide.GlideUtils
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.*
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.log.logger_d
import com.util.lib.span.SpannableImpl
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RepeatConfirmActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_IDS = "key_ids"
        const val EXTRA_ORDER_IDS = "key_order_ids"
    }

    private val mBinding by binding<ActivityRepeatConfirmBinding>()

    private val mAdapter by lazy {
        ConfirmProductAdapter(arrayListOf())
    }

    private val mAutoConfirmModel by lazyViewModel<AutoConfirmViewModel>()

    private val mConfirmViewModel by lazyViewModel<FirstConfirmViewModel>()

    private val mInfoViewModel by lazyViewModel<RepeatConfirmViewModel>()

    private val mUploadViewModel by lazyViewModel<UploadViewModel>()

    private var mPrdIds = "" // 上一个页面带过来的产品id
    private var mOrderId = "" // 复贷有待确认订单时传过来
    private var mBankNo = ""

    private val mProcessDialog by lazy {
        UploadDialog(this)
    }

    private var mStartTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStartTime = System.currentTimeMillis()
        setStatusBarColor(Color.WHITE, true)
        mPrdIds = intent.getStringExtra(EXTRA_IDS).orEmpty()
        mOrderId = intent.getStringExtra(EXTRA_ORDER_IDS).orEmpty()
        mBinding.toolbar.setCustomClickListener {
            showCustomDialog()
        }
        mBinding.toolbar.setOnbackListener {
            finish()
        }
        setViewModelLoading(mInfoViewModel)
        setViewModelLoading(mAutoConfirmModel)
        lifecycle.addObserver(mAutoConfirmModel)
        setAdapter()
        mBinding.aivArrow.setBlockingOnClickListener(this)
        mBinding.tvConfirm.setBlockingOnClickListener(this)
        mBinding.iilBank.setBlockingOnClickListener(this)
        mBinding.tvCancel.setBlockingOnClickListener(this)

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
                mProcessDialog.end()
                MainHandler.postDelay({
                    mProcessDialog.dismiss()
                    LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
                    Launch.skipApplySuccessActivity(this)
                    finish()
                }, 260)
            } else {
                mProcessDialog.dismiss()
                it.ShowErrorMsg(::confirm)
            }
        }

        mInfoViewModel.mConfirmInfoLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@observerNonSticky
                mBankNo = data.KcGqvf.orEmpty()
                mBinding.iilBank.setRightText(maskBank(data.KcGqvf))
                mBinding.iilRepayDate.setRightText(data.jcSrg9.orEmpty())
                setText(data.bwAK6N3EuE.orEmpty(), data.FXE6B.orEmpty(), data.BM3HTNDY1b.orEmpty())
            } else {
                it.ShowErrorMsg(::getInfo)
            }
        }

        mInfoViewModel.mProListLiveData.observerNonSticky(this) {
            // 获取自动确认确认额度时间
            mAutoConfirmModel.getDownTimeMill(mOrderId, it.firstOrNull()?.Bwh8vVa5wn.orEmpty())
            addHorizontalProduct(it)
            mAdapter.setItems(it)
        }

        LiveDataBus.getLiveData(BankEvent::class.java).observerNonSticky(this) {
            if (it.evnet == BankEvent.EVENT_BANK) {
                if (!it.params.isNullOrEmpty()) {
                    mBankNo = it.params.orEmpty()
                    mBinding.iilBank.setRightText(maskBank(mBankNo))
                }
            }
        }
        mUploadViewModel.resultLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                confirm()
            } else {
                mProcessDialog.dismiss()
                it.ShowErrorMsg(::reqPermission)
            }
        }

        mAutoConfirmModel.downTimerLiveData.observerNonSticky(this) {
            if (it < 0) {
                // 获取数据，自动确认额度接口
                hideAutoView()
                reqPermission()
            } else {
                mBinding.tvdAuto.show()
                mBinding.tvCancel.show()
                val param = timeToTimeStr(it, TimeUnit.SECONDS, true)
                val text = getString(R.string.auto_confirm_hint, param)
                val span = SpannableImpl().init(text)
                    .color(ContextCompat.getColor(this, R.color.color_FE4F4F), param)
                    .getSpannable()
                mBinding.tvdAuto.text = span
            }
        }

        mAutoConfirmModel.cancelLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                mAutoConfirmModel.stopCountDown()
                hideAutoView()
            } else it.ShowErrorMsg(::cancelLoan)
        }
    }

    private fun cancelLoan() {
        mAutoConfirmModel.cancel(mOrderId, getOrderIds())
    }

    private fun hideAutoView() {
        mBinding.tvdAuto.hide()
        mBinding.tvCancel.hide()
    }

    private fun setText(product: String, interest: String, loan: String) {
        mBinding.tvAmount.text = getUnitString(product)
        mBinding.iilTotalAmount.setRightText(getUnitString(product))
        mBinding.iilAmount.setRightText(getUnitString(loan))
        mBinding.iilTotalInterest.setRightText(getUnitString(interest))
    }

    private fun getInfo() {
        mInfoViewModel.getConfirmInfo(productIds = mPrdIds, orderId = mOrderId)

    }

    private var mRHeight: Int = 0

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.tv_confirm -> {
                reqPermission()
            }
            R.id.aiv_arrow -> {
                // 底部list展开或收起
                mBinding.aivArrow.isEnabled = false
                val duration = mAdapter.itemCount * 120L
                mBinding.aivArrow.animate().rotationBy(180f).setDuration(duration)
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
                AnimtorUtils.startAnima(mBinding.recyclerview, start, end, mRHeight, duration) {
                }
            }
            R.id.iil_bank -> {
                Launch.skipBankCardListActivity(
                    this,
                    mAdapter.getTotalAmount().toString(),
                    getOrderIds(),
                    mBankNo
                )
            }
            R.id.tv_cancel -> {
                CancelAutoHintDialog(this)
                    .setAmount(mAdapter.getTotalAmount().toString())
                    .setConfirmListener {
                        cancelLoan()
                    }
                    .show()
            }
        }
    }


    private fun reqPermission() {
        PermissionHelper.reqPermission(
            this,
            appPermissions.toList(),
            true,
            isFixGroup = true,
            {
                mProcessDialog.show()
                mUploadViewModel.checkAndUpload()
            },
            {
                jumpToAppSettingPage()
            })
    }


    private fun getOrderIds() = mAdapter.getSelectorList().map { it.ekrpWqU0 }.joinToString(",")

    private fun confirm() {
        mConfirmViewModel.confirmLoan(mBankNo, getOrderIds())
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
                val data = mAdapter.getItemData<RspRepeatCalcul.CalculDetail>(position)
                if (data?.isCheck == 1 && mAdapter.getSelectorList().size <= 1){
                    toast(R.string.toast_min_product)
                    return
                }
                data?.change()
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
                R.drawable.ic_normal_image
            )
            binding.tvName.text = product.um7clL0I
        }
        mBinding.llItem.requestLayout()
        mBinding.llProductList.requestLayout()
    }

    override fun onDestroy() {
        loanPageStayTime = System.currentTimeMillis() - mStartTime
        super.onDestroy()
    }
}