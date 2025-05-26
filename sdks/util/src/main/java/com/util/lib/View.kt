package com.util.lib

import android.view.View


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.ifShow(isShow: Boolean) {
    if (isShow) show() else hide()
}

fun View.isHide() = visibility == View.GONE