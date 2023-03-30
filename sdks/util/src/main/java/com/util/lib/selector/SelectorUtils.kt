package com.util.lib.selector

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.View

/**
 * Created by weishl on 2022/1/11
 *
 */
class SelectorUtils {

    private val drawable by lazy(LazyThreadSafetyMode.NONE) {
        StateListDrawable()
    }

    fun createSelected(selectDrawable: Drawable, normalDrawable: Drawable): SelectorUtils {
        drawable.addState(intArrayOf(android.R.attr.state_selected), selectDrawable)
        drawable.addState(intArrayOf(-android.R.attr.state_selected), selectDrawable)
        drawable.addState(intArrayOf(), normalDrawable)
        return this
    }

    fun createEnabled(enableDrawable: Drawable, normalDrawable: Drawable): SelectorUtils {
        drawable.addState(intArrayOf(android.R.attr.state_enabled), enableDrawable)
        drawable.addState(intArrayOf(-android.R.attr.state_enabled), normalDrawable)
        drawable.addState(intArrayOf(), normalDrawable)
        return this
    }

    fun createChecked(checkDrawable: Drawable, normalDrawable: Drawable): SelectorUtils {
        drawable.addState(intArrayOf(android.R.attr.state_checked), checkDrawable)
        drawable.addState(intArrayOf(-android.R.attr.state_checked), normalDrawable)
        drawable.addState(intArrayOf(), normalDrawable)
        return this
    }

    fun createPressed(pressDrawable: Drawable, normalDrawable: Drawable): SelectorUtils {
        drawable.addState(intArrayOf(android.R.attr.state_pressed), pressDrawable)
        drawable.addState(intArrayOf(-android.R.attr.state_pressed), normalDrawable)
        drawable.addState(intArrayOf(), normalDrawable)
        return this
    }

    fun getSelectorDrawable() = drawable

    fun into(view: View) {
        view.setBackgroundDrawable(drawable)
    }
}