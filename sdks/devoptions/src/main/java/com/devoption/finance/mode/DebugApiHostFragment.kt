package com.devoption.finance.mode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.devoption.finance.DebugApiConfigInfo
import com.devoption.finance.DebugBaseFragment
import com.okredito.devoptions.R
import com.okredito.devoptions.databinding.FragmentApihostBinding

/**
 * Created by weishl on 2021/7/12
 *
 */
internal class DebugApiHostFragment : DebugBaseFragment() {


    private var _mBinding: FragmentApihostBinding? = null
    private val binding = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentApihostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onCheckListener = OnCheckedChangeImpl()
        binding.radioGroup.setOnCheckedChangeListener(onCheckListener)
        binding.h5RadioGroup.setOnCheckedChangeListener(onCheckListener)
        binding.bigDataRadioGroup.setOnCheckedChangeListener(onCheckListener)

        binding.radioGroup.check(if (DebugApiConfigInfo.isApiDebug) R.id.api_rb_debug else R.id.api_rb_release)
        binding.h5RadioGroup.check(if (DebugApiConfigInfo.isH5Debug) R.id.h5_rb_debug else R.id.h5_rb_release)
        binding.bigDataRadioGroup.check(if (DebugApiConfigInfo.isBigDataDebug) R.id.bigData_rb_debug else R.id.bigData_rb_release)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
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