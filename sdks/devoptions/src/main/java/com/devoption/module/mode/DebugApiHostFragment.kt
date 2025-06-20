package com.devoption.module.mode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.devoption.module.DebugApiConfigInfo
import com.devoption.module.DebugBaseFragment
import com.devoption.module.DebugModeHolder
import com.devoption.module.R
import com.devoption.module.databinding.FragmentApihostBinding

internal class DebugApiHostFragment : DebugBaseFragment() {

    private lateinit var mBinding: FragmentApihostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentApihostBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onCheckListener = OnCheckedChangeImpl()
        mBinding.radioGroup.setOnCheckedChangeListener(onCheckListener)
        mBinding.h5RadioGroup.setOnCheckedChangeListener(onCheckListener)
        mBinding.bigDataRadioGroup.setOnCheckedChangeListener(onCheckListener)

        DebugModeHolder.mCurrApiModeDebug = DebugApiConfigInfo.isApiDebug
        mBinding.radioGroup.check(if (DebugApiConfigInfo.isApiDebug) R.id.api_rb_debug else R.id.api_rb_release)
        mBinding.h5RadioGroup.check(if (DebugApiConfigInfo.isH5Debug) R.id.h5_rb_debug else R.id.h5_rb_release)
        mBinding.bigDataRadioGroup.check(if (DebugApiConfigInfo.isBigDataDebug) R.id.bigData_rb_debug else R.id.bigData_rb_release)
    }

    class OnCheckedChangeImpl : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            when (checkedId) {
                R.id.api_rb_debug -> {
                    DebugApiConfigInfo.isApiDebug = true
                }
                R.id.api_rb_release -> {
                    DebugApiConfigInfo.isApiDebug = false
                }
                R.id.h5_rb_debug -> {
                    DebugApiConfigInfo.isH5Debug = true
                }
                R.id.h5_rb_release -> {
                    DebugApiConfigInfo.isH5Debug = false
                }
                R.id.bigData_rb_debug -> {
                    DebugApiConfigInfo.isBigDataDebug = true
                }
                R.id.bigData_rb_release -> {
                    DebugApiConfigInfo.isBigDataDebug = false
                }
            }
        }
    }
}