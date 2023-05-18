package com.colombia.credit.module.login

import android.os.Bundle
import android.text.Editable
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bigdata.lib.loginTime
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentLoginBinding
import com.colombia.credit.expand.*
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.manager.InputHelper
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.view.SysMobileLayout
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.expand.showSoftInput
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.SysUtils
import com.util.lib.hide
import com.util.lib.ifShow
import com.util.lib.show
import com.util.lib.span.SpannableImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

// 登录页面
@AndroidEntryPoint
class LoginFragment : BaseLoginFragment() {
    private val mBinding by binding(FragmentLoginBinding::inflate)

    private val mViewModel by lazyViewModel<LoginViewModel>()

    private val mSmsHelper by lazy(LazyThreadSafetyMode.NONE) {
        SmsCodeHelper()
    }

    private val mLoginHelper by lazy {
        LoginHelper()
    }

    private var isAutoGetMobile: Boolean = false

    private var mMobileLayout: SysMobileLayout? = null
        get() {
            return SysMobileLayout(getSupportContext()).also {
                it.setItemClick { item ->
                    isAutoGetMobile = false
                    mBinding.loginEditPhone.setText(item)
                    mBinding.clInput.removeView(it)
                    mMobileLayout = null
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.loginToolbar)
        setViewModelLoading(mViewModel)
        lifecycle.addObserver(mViewModel)
        lifecycle.addObserver(mSmsHelper)
        lifecycle.addObserver(mLoginHelper)
        mBinding.loginEditPhone.requestFocus()
        showSoftInput(mBinding.loginEditPhone)
        initProtocol()
        initText()
        mBinding.loginTvVoice.isEnabled = true
        initObserver()
        initView()

        mBinding.loginEditPhone.postDelayed({
            if (isDestroyView()) return@postDelayed
            getSysMobile()
        }, 600)
    }

    // 获取手机号
    private fun getSysMobile() {
        val mobiles = SysUtils.getPhoneNumbers(getSupportContext())
        if (mobiles.isNullOrEmpty()) return
        if (mobiles.size == 1) {
            isAutoGetMobile = true
            val text = mobiles[0]
            mBinding.loginEditPhone.setText(text)
            mBinding.loginEditPhone.setSelection(text.length)
            return
        }
        mMobileLayout?.let {layout ->
            layout.setData(mobiles)
            val locations = IntArray(2)
            mBinding.loginEditPhone.getLocationInWindow(locations)
            val finalX = mBinding.loginEditPhone.x.roundToInt()
            val finalY = mBinding.llMobile.y + mBinding.llMobile.height
            layout.x = finalX.toFloat()
            layout.y = finalY
            val layoutWidth = ((mBinding.llMobile.width - finalX) * 0.88f).toInt()
            val layoutParams = ConstraintLayout.LayoutParams(layoutWidth, LayoutParams.WRAP_CONTENT)
            mBinding.clInput.addView(layout, layoutParams)
        }
        hideSoftInput()
    }

    private fun initView() {
        mBinding.loginEditPhone.onFocusChangeListener = object : OnFocusChangeListener {
            var startTime = System.currentTimeMillis()
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    if (getMobile().isNotEmpty()) {
                        loginTime = System.currentTimeMillis() - startTime
                    }
                } else {
                    startTime = System.currentTimeMillis()
                }
            }
        }

        mBinding.loginTvOtp.setBlockingOnClickListener {
            if (getMobile().length < 10) {
                mBinding.loginTvPhoneError.show()
                return@setBlockingOnClickListener
            }
            mSmsHelper.updateReceiverTime()
            mLoginHelper.isFirst(getMobile())
            reqCode()
        }
        mBinding.loginTvBtn.setBlockingOnClickListener {
            login()
        }
        mBinding.loginTvVoice.setBlockingOnClickListener {
            reqCode(false, LoginViewModel.TYPE_VOICE)
        }


        mBinding.loginEditPhone.addTextChangedListener(object : InputHelper.TextWatchAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                mBinding.loginTvPhoneError.hide()
            }

            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                s?.let {
                    if (s.length >= 10) {
                        val text = s.toString()
                        if (mLoginHelper.isFirst(text)) {
                            reqCode(true)
                        }
                    }
                }
            }
        })
        mBinding.loginEditCode.addTextChangedListener(object : InputHelper.TextWatchAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                mBinding.loginTvCodeError.hide()
            }

            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if ((s?.length ?: 0) < 4) {
                    mSmsHelper.isAutoInsert = false
                }
                s?.let {
                    // 非自动触发获取验证码 || 非自动回填验证码 会自动触发登录接口
                    if (s.length == 4 && !(mLoginHelper.isFirstAuto(mBinding.loginEditPhone.getRealText()) || mSmsHelper.isAutoInsert)
                        && !mViewModel.isAutoGetCode && !isAutoGetMobile
                    ) {
                        isAutoGetMobile = false
                        login()
                    }
                }
            }
        })
    }

    private fun initObserver() {
        mSmsHelper.registerObserver(mBinding.loginEditCode)
        mViewModel.downTimerLiveData.observerNonSticky(viewLifecycleOwner) { time ->
            if (time == -1L) {
                mBinding.loginTvVoice.isEnabled = true
                mBinding.loginTvOtp.isEnabled = true
                mBinding.loginTvOtp.setText(R.string.sms_otp)
            } else {
                mBinding.loginTvOtp.text = getString(R.string.seconds, "$time")
            }
        }

        mViewModel.mVoiceLiveData.observerNonSticky(viewLifecycleOwner) {
            mBinding.loginTvVoice.ifShow(it)
        }

        mViewModel.mAuthSmsCodeLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                mViewModel.startCountdown(type = mViewModel.getCurrCodeType(), mobile = getMobile())
                mBinding.loginTvOtp.isEnabled = false
                if (mViewModel.getCurrCodeType() == LoginViewModel.TYPE_VOICE) {
                    mBinding.loginTvVoice.isEnabled = false
                }
            } else {
                it.ShowErrorMsg(::reqCode)
            }
        }
        mViewModel.loginLiveData.observerNonSticky(viewLifecycleOwner) {
            if (it.isSuccess()) {
                if (isNewUser) {
                    jumpProcess(getSupportContext(), STEP1)
                }
                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_LOGIN))
            } else {
                it.ShowErrorMsg(::login)
            }
        }

        mSmsHelper.codeLivedata.observerNonSticky(viewLifecycleOwner) {
            mViewModel.cancelDown30()
        }
    }


    private fun initText() {
        val voiceParam = getString(R.string.login_voice_params)
        val voiceText = getString(R.string.login_voice, voiceParam)
        mBinding.loginTvVoice.text = SpannableImpl().init(voiceText).commonParams(voiceParam)
            .color(ContextCompat.getColor(getSupportContext(), R.color.colorPrimary))
            .getSpannable()
    }

    private fun initProtocol() {
        val param = getString(R.string.protocol_params)
        val protocol = getString(R.string.login_protocol, param)
        val span = SpannableImpl().init(protocol)
            .color(ContextCompat.getColor(getSupportContext(), R.color.colorPrimary), param)
            .getSpannable()
        mBinding.loginTvProtocol.movementMethod = LinkMovementMethod()
        mBinding.loginTvProtocol.text = span
        mBinding.loginTvProtocol.setBlockingOnClickListener {
            Launch.skipWebViewActivity(getSupportContext(), H5UrlManager.URL_PRIVACY)
        }
    }

    private fun reqCode(isAuto: Boolean = false, type: Int = LoginViewModel.TYPE_SMS) {
        mBinding.loginEditCode.setText("")
        mBinding.loginEditCode.requestFocus()
        showSoftInput(mBinding.loginEditCode)
        mViewModel.isDown30Auto = false
        reqSmsCode(isAuto, type)
    }

    private fun login() {
        val mobile = getMobile()
        val code = mBinding.loginEditCode.getRealText()
        if (!checkMobile(mobile)) {
            mBinding.loginTvPhoneError.show()
            return
        }
        if (code.length != 4) {
            mBinding.loginTvCodeError.show()
            return
        }
        mViewModel.reqLogin(mobile, code)
    }

    private fun reqSmsCode(isAuto: Boolean, type: Int = LoginViewModel.TYPE_SMS) {
        mViewModel.reqSmsCode(getMobile(), isAuto, type)
    }

    private fun getMobile() = mBinding.loginEditPhone.getRealText()

    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (visible) {
            getBaseActivity()?.setStatusBarColor(
                ContextCompat.getColor(
                    getSupportContext(),
                    R.color.colorPrimary
                ), false
            )
        } else {
            hideSoftInput()
        }
    }

}