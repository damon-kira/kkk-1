package com.util.lib.expand

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by weishl on 2022/11/24
 *
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
    imm?.hideSoftInputFromWindow(
        view.windowToken,
        InputMethodManager.HIDE_IMPLICIT_ONLY.and(InputMethodManager.HIDE_NOT_ALWAYS)
    ) //强制隐藏键盘
}

fun showSoftInput(view: View){
    view.requestFocus()
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}