package com.util.lib.span

import android.content.Context
import android.text.SpannableString
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.StringRes

interface ISpannable {

    fun init(text: String): ISpannable

    fun init(context: Context, @StringRes textRes: Int): ISpannable

    fun init(context: Context, @StringRes textRes: Int, vararg text: String): ISpannable

    fun commonParams(@NonNull vararg params: String):ISpannable

    /**
     *  指定文案设置字体大小
     *
     * */
    fun size(start: Int, end: Int,textSize: Int): ISpannable

    fun size(textSize: Int, vararg text: String): ISpannable

    fun size(textSize: Int): ISpannable

    /**
     * 指定文案设置加粗字体
     */
    fun bold(start: Int, end: Int): ISpannable

    fun bold(vararg text: String): ISpannable

    fun bold(): ISpannable

    /**
     * 指定文案设置颜色
     */
    fun color(start: Int, end: Int,@ColorInt color: Int): ISpannable

    fun color(@ColorInt color: Int,vararg text: String): ISpannable

    fun color(@ColorInt color: Int): ISpannable

    /**
     * 指定文案设置斜体
     */
    fun italic(start: Int, end: Int): ISpannable

    fun italic(vararg text: String): ISpannable

    fun italic(): ISpannable

    fun getSpannable(): SpannableString
}