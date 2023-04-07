package com.colombia.credit.module.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentAccountBinding
import com.colombia.credit.dialog.CustomDialog
import com.colombia.credit.expand.*
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.MainEvent
import com.common.lib.base.BaseFragment
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.util.lib.hide
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MineFragment : BaseFragment(), View.OnClickListener {

    private val mBinding by binding(FragmentAccountBinding::inflate)

    private val mCustomDialog by lazy {
        CustomDialog(getSupportContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isLogin = !inValidToken()
        mBinding.aivHead.isSelected = isLogin
        if (isLogin) {
            mBinding.tvMobile.show()
            mBinding.tvName.text = getString(R.string.me_hi, getUserName())
            mBinding.tvMobile.text = maskString(getMobile(), 3, 4)
        } else {
            mBinding.tvMobile.hide()
            mBinding.clRepay.hide()
            mBinding.tvRefused.hide()
        }
        // 是否是拒绝状态
        if (isLogin) {
            if (isOrderRefused()) {
                mBinding.clRepay.hide()
                mBinding.tvRefused.show()
            } else {
                mBinding.clRepay.show()
                mBinding.tvRefused.hide()
                // 是否有在贷订单
                var amount = ""
                mBinding.tvRepayAmount.text = getString(R.string.amount_unit, formatCommon(amount))
                if (isLoanOrder()) {
                    mBinding.tvText2.show()
                    mBinding.etvBtn.setText(R.string.me_repayment)
                    mBinding.tvText.setText(R.string.me_repay_text1)
                } else {
                    mBinding.tvText2.hide()
                    mBinding.tvText.setText(R.string.me_loan_amount_hint)
                }
            }
        }

        mBinding.etvCustom.setBlockingOnClickListener(this)
        mBinding.ailAbout.setBlockingOnClickListener(this)
        mBinding.ailFeedback.setBlockingOnClickListener(this)
        mBinding.ailProtocol.setBlockingOnClickListener(this)
        mBinding.ailSetting.setBlockingOnClickListener(this)
        mBinding.flHistory.setBlockingOnClickListener(this)
        mBinding.flBank.setBlockingOnClickListener(this)
        mBinding.etvBtn.setBlockingOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.etv_custom -> {
                mCustomDialog.show()
            }
            R.id.ail_about -> {
                Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_ABOUT)
            }
            R.id.ail_feedback -> {
                if (!checkLogin()) return
                Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_FEEDBACK)
            }
            R.id.ail_protocol -> {
                Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_PRIVACY_PROTOCOL)
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
            R.id.etv_custom -> {
                CustomDialog(getSupportContext()).show()
            }
            R.id.etv_btn -> {
                // 需要区分状态，还款状态--还款页面
                // 没有在贷，跳转首页
                LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
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
}