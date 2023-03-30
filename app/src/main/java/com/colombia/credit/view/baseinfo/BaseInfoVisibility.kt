package com.colombia.credit.view.baseinfo

import android.view.View

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class BaseInfoVisibility {
    companion object {
        const val GONE = 0
        const val VISIBLE = 1
        const val INVISIBLE = 2

        fun getViewVisible(visible: Int): Int {
            return when (visible) {
                GONE -> View.GONE
                INVISIBLE -> View.INVISIBLE
                else -> View.VISIBLE
            }
        }
    }
}