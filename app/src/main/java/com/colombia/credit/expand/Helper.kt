package com.colombia.credit.expand

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.colombia.credit.R


fun setAgreementClickableSpan(
    @ColorRes colorRes: Int = R.color.colorPrimary,
    context: Context,//防止内存泄漏，上下文对象使用applicationContext
    spanText: SpannableStringBuilder,
    clickableText: String,
    onClick: () -> Unit
) {
    val index = spanText.indexOf(clickableText)
    if (index < 0) return
    spanText.setSpan(object : ClickableSpan() {
        override fun onClick(view: View) {
            onClick?.invoke()
        }

        override fun updateDrawState(paint: TextPaint) {
            super.updateDrawState(paint)
            paint?.apply {
                paint.color = ContextCompat.getColor(context, colorRes)
            }
        }
    }, index, index + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}