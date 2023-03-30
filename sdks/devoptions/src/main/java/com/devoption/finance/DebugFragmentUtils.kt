package com.devoption.finance

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by weishl on 2021/7/12
 *
 */
internal object DebugFragmentUtils {

    /**
     * 切换fragment
     *
     * @param fragmentManger     当前fragmentManager
     * @param viewId             容器id
     * @param fragmentDebug           要显示的fragment
     * @return 返回当前显示的tag
     *
     */
    fun switchFragment(
        fragmentManger: FragmentManager, @IdRes viewId: Int,
        fragmentDebug: DebugBaseFragment, currFragmentTag: String? = null,
        addToBack: Boolean = false
    ): String {
        val currFragment = getCurrFragment(fragmentManger, viewId, currFragmentTag)
        val tag = getTag(fragmentDebug)
        val transaction = fragmentManger.beginTransaction()
        currFragment?.let {
            val currTag = getTag(currFragment)
            if (checkFragmentTag(
                    currTag,
                    tag,
                    fragmentManger,
                    fragmentDebug
                )
            ) return getTag(it)
            transaction.hide(currFragment)
//            hideFragment(fragmentManger, transaction, viewId, currTag)
        }

        val findFragment = findFragment(fragmentManger, tag)
        if (findFragment == null) {
            transaction.add(viewId, fragmentDebug, tag)
        } else {
            fragmentDebug.arguments?.let { arguments ->
                findFragment.arguments = arguments
            }
            transaction.show(findFragment)
        }
        if (addToBack) {
            transaction.addToBackStack(tag)
        }
        transaction.commitAllowingStateLoss()
        return tag
    }

    fun getTag(fragment: Fragment): String = fragment::class.java.simpleName

    fun getCurrFragment(fragmentManger: FragmentManager, @IdRes viewId: Int, tag: String? = null): DebugBaseFragment? {
        val fragment = fragmentManger.findFragmentByTag(tag)
        return if (fragment == null) {
            fragmentManger.findFragmentById(viewId) as? DebugBaseFragment
        } else {
            fragment as? DebugBaseFragment
        }
    }


    private fun checkFragmentTag(
        currentFragmentTag: String,
        tag: String,
        fragmentManger: FragmentManager,
        fragment: Fragment
    ): Boolean {
        if (currentFragmentTag == tag) {
            val fragmentTag = findFragment(fragmentManger, currentFragmentTag)
            if (fragment.arguments != null) {
                fragmentTag?.arguments = fragment.arguments
            }
            if (fragmentTag != null) {
                return true
            }
        }
        return false
    }

    fun findFragment(fragmentManger: FragmentManager, tag: String): Fragment? {
        return fragmentManger.findFragmentByTag(tag)
    }
}