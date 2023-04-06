package com.colombia.credit.module.login

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.colombia.credit.manager.Launch
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentLoginBinding
import com.colombia.credit.expand.showNetErrorDialog
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.H5UrlManager
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.show
import com.util.lib.span.SpannableImpl
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
        val param = getString(R.string.protocol_params)
        val protocol = getString(R.string.login_protocol, param)
        val span = SpannableImpl().init(protocol)
            .color(ContextCompat.getColor(getSupportContext(), R.color.colorPrimary), param)
            .getSpannable()
        mBinding.loginTvProtocol.movementMethod = LinkMovementMethod()
        mBinding.loginTvProtocol.text = span
        mBinding.loginTvProtocol.setBlockingOnClickListener {
            // 跳转协议页面
            Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_PRIVACY_PROTOCOL)
        }

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
                toast(it.message.orEmpty())
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
            Launch.skipFaceActivity(getSupportContext())
        }
    }

    private fun reqSmsCode() {
        mViewModel.reqSmsCode(getMobile())
    }

    private fun getMobile() = mBinding.loginEditPhone.getRealText()

}