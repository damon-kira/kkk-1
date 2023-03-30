package com.util.lib

import android.view.View

/**
 * author: weishl
 * data: 2020/12/8
 **/
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.ifShow(isShow: Boolean) {
    if (isShow) show() else hide()
}

fun View.isHide() = visibility == View.GONE