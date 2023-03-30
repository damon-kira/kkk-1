package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.R
import com.colombia.credit.databinding.DialogKycHintBinding
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class KycHintDialog constructor(context: Context): DefaultDialog(context) {

    private val mBinding by binding<DialogKycHintBinding>()

    init {
        setContentView(mBinding.root)
        setDisplaySize(0.9f, WRAP)
        mBinding.kycAivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TYPE_FRONT = 1
        const val TYPE_BACK = 2
    }
    private var mCurrType = TYPE_FRONT

    fun setType(type: Int): KycHintDialog {
        this.mCurrType = type
        return this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        changeType()
    }

    private fun changeType() {
        if (mCurrType == TYPE_FRONT) {
            mBinding.kycEtvText.setText(R.string.kyc_dialog_front)
            mBinding.kycAivTopImage.setImageResource(R.drawable.image_kyc_front)
            mBinding.kycTvError1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.image_kyc_front1,0,0)
            mBinding.kycTvError2.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.image_kyc_front2,0,0)
            mBinding.kycTvError3.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.image_kyc_front3,0,0)
        } else {
            mBinding.kycEtvText.setText(R.string.kyc_dialog_back)
            mBinding.kycAivTopImage.setImageResource(R.drawable.image_kyc_back)
            mBinding.kycTvError1.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.image_kyc_back1,0,0)
            mBinding.kycTvError2.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.image_kyc_back2,0,0)
            mBinding.kycTvError3.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.image_kyc_back3,0,0)
        }

    }

    fun setOnClickListener(listener: () -> Unit): KycHintDialog{
        mBinding.kycDialogTvBtn.setBlockingOnClickListener {
            listener.invoke()
            dismiss()
        }
        return this
    }

}