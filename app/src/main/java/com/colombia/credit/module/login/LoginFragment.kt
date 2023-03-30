package com.colombia.credit.module.login

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colombia.credit.Launch
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentLoginBinding
import com.colombia.credit.dialog.AddressSelectorDialog
import com.colombia.credit.dialog.FirstLoanHintDialog
import com.colombia.credit.dialog.KycHintDialog
import com.colombia.credit.dialog.ProcessBackDialog
import com.colombia.credit.expand.showNetErrorDialog
import com.colombia.credit.expand.toast
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by weishl on 2023/3/27
 *
 */
@AndroidEntryPoint
class LoginFragment : BaseLoginFragment() {
    private val mBinding by binding(FragmentLoginBinding::inflate)
    private val mViewModel by lazyViewModel<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.downTimerLiveData.observerNonSticky(viewLifecycleOwner) { time ->
            if (time == -1L) {
                mBinding.loginTvOtp.setText(R.string.sms_otp)
            } else {
                mBinding.loginTvOtp.text = getString(R.string.seconds, "$time")
            }
        }

        mViewModel.mAuthSmsCodeLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                mViewModel.startCountdown(mobile = getMobile())
            } else {
                if (it.e is NetworkErrorException) {
                    getBaseActivity()?.showNetErrorDialog {
                        reqSmsCode()
                    }
                }
                toast("获取验证码失败")
            }
        }

        mBinding.loginTvOtp.setBlockingOnClickListener {
            if (getMobile().length < 10) {
                mBinding.loginTvPhoneError.show()
                return@setBlockingOnClickListener
            }
            reqSmsCode()
        }
        mBinding.loginTvBtn.setBlockingOnClickListener {
            Launch.skipKycInfoActivity(view.context)
        }
    }

    private fun reqSmsCode() {
        mViewModel.reqSmsCode(getMobile())
    }

    private fun getMobile() = mBinding.loginEditPhone.getRealText()

}