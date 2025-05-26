package com.common.lib.base

import android.view.MotionEvent

interface IFragmentSwitch {

    fun switchFragment(fragment: BaseFragment)

    fun replaceFragment(fragment: BaseFragment)

    fun showToFragment(fragment: BaseFragment)

    fun reloadRootFragment(fragment: BaseFragment)

    fun pop(): Boolean
}

interface IFragmentTouch {

    fun onTouchEvent(ev: MotionEvent?)
}