package com.common.lib.base

import android.view.ViewGroup

/**
 * 软键盘适配
 **/
interface ISoftAdapter {
    fun getScrollView(): ViewGroup?

    fun getTargetY(): Int

    fun getMarginBottom(): Float

    fun isDestroyViewSoft(): Boolean
}