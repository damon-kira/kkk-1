package com.common.lib.expand

import android.view.View

/**
 * Created by weisl on 2019/10/17.
 */

fun View.setBlockingOnClickListener(listener: View.OnClickListener) {
    setOnClickListener(object : BlockingOnClickListener() {
        override fun onValidClick(view: View?) {
            listener.onClick(view)
        }
    })
}

fun View.setBlockingOnClickListener(listener: (view: View) -> Unit) {
    setOnClickListener(object : BlockingOnClickListener() {
        override fun onValidClick(view: View?) {
            view ?: return
            listener(view)
        }
    })
}


abstract class BlockingOnClickListener(
    private val thresholdMs: Long = 800L
) : View.OnClickListener {
    private var preTimesMill = 0L

    abstract fun onValidClick(view: View?)

    final override fun onClick(view: View?) {
        val prev = preTimesMill
        val curr = System.currentTimeMillis()
        if (isWithinThreshold(curr, prev)) {
            return
        }
        preTimesMill = curr
        onValidClick(view)
    }

    private fun isWithinThreshold(curr: Long, prev: Long) = (curr - prev) <= thresholdMs
}