package com.common.lib.base

import android.view.MotionEvent
import androidx.annotation.IdRes
import com.common.lib.helper.FragmentHelper

/**
 * Created by weisl on 2019/10/29.
 */
abstract class BaseFragmentActivity : BaseActivity(), IFragmentSwitch {

    protected var mCurrTag: String? = null

    override fun switchFragment(fragment: BaseFragment) {
        mCurrTag = FragmentHelper.switchFragment(supportFragmentManager, getFragmentViewId(), fragment, mCurrTag)
    }

    override fun replaceFragment(fragment: BaseFragment) {
        mCurrTag = FragmentHelper.replaceFragment(supportFragmentManager, getFragmentViewId(), fragment, mCurrTag)
    }

    override fun showToFragment(fragment: BaseFragment) {
        mCurrTag = FragmentHelper.popToFragment(supportFragmentManager, getFragmentViewId(), fragment)
    }

    override fun reloadRootFragment(fragment: BaseFragment) {
        mCurrTag = FragmentHelper.reloadRootFragment(supportFragmentManager, getFragmentViewId(), fragment)
    }

    protected fun getCurrFragment(): BaseFragment? {
        return FragmentHelper.getCurrFragment(supportFragmentManager, getFragmentViewId(), mCurrTag)
    }

    override fun pop(): Boolean {
        val result = FragmentHelper.pop(supportFragmentManager)
        if (result) {
            FragmentHelper.getTopFragment(supportFragmentManager)?.let {
                mCurrTag = FragmentHelper.getTag(it)
            }
        }
        return result
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        getCurrFragment()?.let {
            if (it is IFragmentTouch) {
                it.onTouchEvent(ev)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @IdRes
    abstract fun getFragmentViewId(): Int

    override fun onBackPressed() {
        val currFragment = FragmentHelper.getCurrFragment(supportFragmentManager, getFragmentViewId(), mCurrTag)
        if (currFragment?.onFragmentBackPressed() == false || currFragment == null) {
            super.onBackPressed()
        }
    }
}