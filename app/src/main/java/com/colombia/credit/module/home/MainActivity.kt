package com.colombia.credit.module.home

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityMainBinding
import com.colombia.credit.module.account.MineFragment
import com.colombia.credit.module.repay.RepayTabFragment
import com.common.lib.base.BaseFragment
import com.common.lib.base.BaseFragmentActivity
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.LiveDataBusObserve
import com.common.lib.livedata.observerNonStickyForever
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseFragmentActivity() {

    private val mBinding: ActivityMainBinding by binding()

    private val mHomeViewModel by lazyViewModel<HomeLoanViewModel>()

    private val mHomeFragment by lazy(LazyThreadSafetyMode.NONE) {
        BaseFragment.getInstance(this, HomeFragment::class.java)
    }

    private val mRepayFragment by lazy(LazyThreadSafetyMode.NONE) {
        BaseFragment.getInstance(this, RepayTabFragment::class.java)
    }

    private val mMineFragment by lazy(LazyThreadSafetyMode.NONE) {
        BaseFragment.getInstance(this, MineFragment::class.java)
    }

    private var mMainEventObserve: LiveDataBusObserve<MainEvent>? = null

    override fun getFragmentViewId(): Int = R.id.fl_main_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRadioButton()

        mMainEventObserve = LiveDataBus.getLiveData(MainEvent::class.java).observerNonStickyForever {
            if (it.event == MainEvent.EVENT_SHOW_HOME) {
                mBinding.rbHomeLoan.isChecked = true
            } else if (it.event == MainEvent.EVENT_SHOW_REPAY) {
                mBinding.rbHomeRepay.isChecked = true
            }
        }
    }


    private fun initRadioButton() {
        mBinding.rbHomeLoan.buttonDrawable = StateListDrawable()
        mBinding.rbHomeRepay.buttonDrawable = StateListDrawable()
        mBinding.rbAccount.buttonDrawable = StateListDrawable()

        mBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_home_loan -> {
                    switchFragment(mHomeFragment)
                }
                R.id.rb_home_repay -> {
                    switchFragment(mRepayFragment)
                    try {
                        supportFragmentManager.executePendingTransactions()
                    } catch (e: Exception) {

                    }
                }
                R.id.rb_account -> {
                    switchFragment(mMineFragment)
                    try {
                        supportFragmentManager.executePendingTransactions()
                    } catch (e: Exception) {

                    }
                }
            }
        }
        mBinding.rbHomeLoan.isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.removeObserve(MainEvent::class.java, mMainEventObserve)
    }
}