package com.colombia.credit.module.account

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentAccountBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.home.MainEvent
import com.colombia.credit.module.home.OrderStatus
import com.common.lib.base.BaseFragment
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.hide
import com.util.lib.ifShow
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineFragment : BaseFragment(), View.OnClickListener {

    private val mBinding by binding(FragmentAccountBinding::inflate)

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private var mStatus: Int = 0 // 大于0，跳转还款页面

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeUserName()

        mBinding.etvCustom.setBlockingOnClickListener(this)
        mBinding.ailAbout.setBlockingOnClickListener(this)
        mBinding.ailFeedback.setBlockingOnClickListener(this)
        mBinding.ailProtocol.setBlockingOnClickListener(this)
        mBinding.ailSetting.setBlockingOnClickListener(this)
        mBinding.flHistory.setBlockingOnClickListener(this)
        mBinding.flBank.setBlockingOnClickListener(this)
        mBinding.etvBtn.setBlockingOnClickListener(this)
        mBinding.aivHead.setBlockingOnClickListener(this)
        mBinding.llUserinfo.setBlockingOnClickListener(this)

        initObserver()
    }

    private fun initObserver() {
        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            when (it.xXkO) {
                OrderStatus.STATUS_FIRST_PRODUCT -> {
                    mStatus = 0
                    mBinding.clRepay.show()
                    mBinding.tvRefused.hide()
                    mBinding.tvAmount.text = getUnitString(it.yqGhrjOF2.orEmpty())
                    mBinding.etvBtn.setText(R.string.me_loan)
                    mBinding.etvBtn.isSelected = true
                    mBinding.tvText.setText(R.string.me_loan_amount_hint)
                    mBinding.tvText2.hide()
                }
                OrderStatus.STATUS_REPAY, OrderStatus.STATUS_OVERDUE,
                OrderStatus.STATUS_REPEAT2, OrderStatus.STATUS_REPEAT4 -> {
                    mStatus = 1
                    mBinding.clRepay.show()
                    mBinding.tvRefused.hide()
                    val amount = if (isRepeat) it.gQ1J?.RPBJ47rhC else it.yqGhrjOF2
                    mBinding.tvAmount.text = getUnitString(amount.orEmpty())
                    mBinding.etvBtn.setText(R.string.me_repayment)
                    if (it.xXkO == OrderStatus.STATUS_OVERDUE) {
                        mBinding.etvBtn.setSolidColorRes(R.color.color_fe4f4f)
                    } else {
                        mBinding.etvBtn.setSolidColorRes(R.color.color_32C558)
                    }
                    mBinding.etvBtn.isSelected = false
                    mBinding.tvText.setText(R.string.me_repay_text1)
                    mBinding.tvText2.show()
                }
                OrderStatus.STATUS_REJECT -> {
                    mBinding.clRepay.hide()
                    mBinding.tvRefused.show()
                    mBinding.tvRefused.text = getString(R.string.me_refused_hint, it.K1v0Pz)
                }
                else -> {
                    mBinding.clRepay.hide()
                    mBinding.tvRefused.hide()
                }
            }
        }
    }

    private fun showBank(isShow: Boolean) {
        mBinding.flBank.ifShow(isShow)
        mBinding.viewLine.ifShow(isShow)
    }

    private fun changeUserName() {
        mBinding.aivHead.isSelected = !inValidToken()
        if (inValidToken()) {
            mBinding.tvMobile.hide()
            mBinding.tvName.setText(R.string.account_not_login)
            mBinding.clRepay.hide()
            mBinding.tvRefused.hide()
        } else {
            mBinding.tvMobile.show()
            mBinding.tvName.text = getString(R.string.me_hi, mUserName)
            mBinding.tvMobile.text = maskString(getMobile(), 3, 4)
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.aiv_head, R.id.ll_userinfo->{
                checkLogin()
            }
            R.id.etv_custom -> {
                getBaseActivity()?.showCustomDialog()
            }
            R.id.ail_about -> {
                Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_ABOUT)
            }
            R.id.ail_feedback -> {
                if (!checkLogin()) return
                Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_FEEDBACK)
            }
            R.id.ail_protocol -> {
                Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_PRIVACY)
            }
            R.id.ail_setting -> {
                if (!checkLogin()) return
                Launch.skipSettingActivity(getSupportContext())
            }
            R.id.fl_history -> {
                if (!checkLogin()) return
                Launch.skipHistoryActivity(getSupportContext())
            }
            R.id.fl_bank -> {
                if (!checkLogin()) return
                Launch.skipMeBankCardListActivity(getSupportContext())
            }
            R.id.etv_btn -> {
                // 需要区分状态，还款状态--还款页面
                // 没有在贷，跳转首页
                if (mStatus > 0) {
                    LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
                } else {
                    LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
                }
            }
        }
    }

    private fun checkLogin(): Boolean {
        if (inValidToken()) {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
            return false
        }
        return true
    }

    private fun changeStatus() {
        if (inValidToken()) {
            mBinding.clRepay.hide()
            mBinding.tvRefused.hide()
        }
    }

    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (visible) {
            changeStatus()
            changeUserName()
            showBank(isRepeat || (orderStatus != OrderStatus.STATUS_FIRST_PRODUCT && orderStatus != null))
            getBaseActivity()?.setStatusBarColor(Color.WHITE, true)
            if (inValidToken()) {
                showBank(false)
            }
        }
    }
}