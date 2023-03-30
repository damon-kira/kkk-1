package com.common.lib.expand

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Create by weishl
 * 2022/8/31
 */


/**
 * 隐藏软键盘
 */
fun hideSoftInput(activity: Activity) {
//    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(context
//            .currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        activity.window.decorView.windowToken,
        0
    )
}

fun hideSoftInput(view: View){
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0) //强制隐藏键盘
}

/**
 * 显示软键盘
 * @param view
 */
fun showSoftInput(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}
