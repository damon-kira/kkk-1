package com.colombia.credit.expand

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.R


const val ERROR_ICON_REPLACE_STRING = "/ic_info"//代替换错误icon的文案
val errorImageSpan: Spannable by lazy {
    //替换完错误icon的ImageSpan
    val spanString = SpannableStringBuilder()
    spanString.append(ERROR_ICON_REPLACE_STRING).append(errorPadding)
    spanString.setSpan(
        ImageSpan(getAppContext(), R.drawable.svg_error_hint),
        0,
        ERROR_ICON_REPLACE_STRING.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spanString
}
private val errorPadding by lazy {
    getAppContext().getString(R.string.error_hint_blank)
}