package com.util.lib.span

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.NonNull
import com.util.lib.BuildConfig
import java.lang.Exception

class SpannableImpl : ISpannable {

    private var mSpannable: SpannableString? = null

    private var mStr: String = ""

    private var mCommonParams = arrayOf<String>()

    override fun init(@NonNull text: String): ISpannable {
        mStr = text
        mSpannable = SpannableString(text)
        return this
    }

    override fun init(@NonNull context: Context, textRes: Int): ISpannable {
        val text = context.getString(textRes)
        mStr = text
        mSpannable = SpannableString(text)
        return this
    }

    override fun init(@NonNull context: Context, textRes: Int,@NonNull vararg text: String): ISpannable {
        val str = context.getString(textRes, *text)
        mStr = str
        mSpannable = SpannableString(str)
        return this
    }

    override fun commonParams(vararg params: String): ISpannable {
        mCommonParams = params.toList().toTypedArray()
        return this
    }

    override fun size(start: Int, end: Int, textSize: Int): ISpannable {
        mSpannable?.setSpan(
            AbsoluteSizeSpan(textSize),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    override fun size(textSize: Int,@NonNull vararg params: String): ISpannable {
        try {
            var str = mStr
            var originIndex = 0
            var indexParam = ""
            for (i in params.indices) {
                indexParam = params[i]
                val index = str.indexOf(indexParam)
                if (index < 0) {
                    if (isDebug()) {
                        throw IllegalArgumentException("size index = -1")
                    }
                    return this
                }
                originIndex += index
                str = str.substring(index + indexParam.length)
                mSpannable?.setSpan(
                    AbsoluteSizeSpan(textSize),
                    originIndex,
                    originIndex + indexParam.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                originIndex += indexParam.length
            }
        } catch (e: Exception) {
            return this
        }
        return this
    }

    override fun size(textSize: Int): ISpannable {
        return size(textSize, *mCommonParams)
    }

    override fun bold(start: Int, end: Int): ISpannable {
        mSpannable?.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return this
    }

    override fun bold(@NonNull vararg params: String): ISpannable {
        try {
            var str = mStr
            var originIndex = 0
            var indexParam = ""
            for (i in params.indices) {
                indexParam = params[i]
                val index = str.indexOf(indexParam)
                if (index < 0) {
                    if (isDebug()) {
                        throw IllegalArgumentException("bold index = -1")
                    }
                    return this
                }
                originIndex += index
                str = str.substring(index + indexParam.length)
                mSpannable?.setSpan(
                    StyleSpan(Typeface.BOLD),
                    originIndex,
                    originIndex + indexParam.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                originIndex += indexParam.length
            }
        } catch (e: Exception) {
            return this
        }

        return this
    }

    override fun bold(): ISpannable {
        return bold(*mCommonParams)
    }

    override fun color(start: Int, end: Int, color: Int): ISpannable {
        mSpannable?.setSpan(
            ForegroundColorSpan(color), start, end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    override fun color(color: Int,@NonNull vararg params: String): ISpannable {
        try {
            var str = mStr
            var originIndex = 0
            var indexParam = ""
            for (i in params.indices) {
                indexParam = params[i]
                val index = str.indexOf(indexParam)
                if (index < 0) {
                    if (isDebug()) {
                        throw IllegalArgumentException("color index = -1")
                    }
                    return this
                }
                originIndex += index
                str = str.substring(index + indexParam.length)
                mSpannable?.setSpan(
                    ForegroundColorSpan(color), originIndex, originIndex + indexParam.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                originIndex += indexParam.length
            }
        } catch (e: Exception) {
            return this
        }
        return this
    }

    override fun color(color: Int): ISpannable {
        return color(color, *mCommonParams)
    }

    override fun italic(start: Int, end: Int): ISpannable {
        mSpannable?.setSpan(
            StyleSpan(Typeface.BOLD_ITALIC),
            start,
            end,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return this
    }

    override fun italic(@NonNull vararg params: String): ISpannable {
        try {
            var str = mStr
            var originIndex = 0
            var indexParam = ""
            for (i in params.indices) {
                indexParam = params[i]
                val index = str.indexOf(indexParam)
                if (index < 0) {
                    if (isDebug()) {
                        throw IllegalArgumentException("italic index = -1")
                    }
                    return this
                }
                originIndex += index
                str = str.substring(index + indexParam.length)
                mSpannable?.setSpan(
                    StyleSpan(Typeface.BOLD_ITALIC),
                    originIndex,
                    originIndex + indexParam.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                originIndex += indexParam.length
            }
        } catch (e: Exception) {
            return this
        }
        return this
    }

    override fun italic(): ISpannable {
        return italic(*mCommonParams)
    }

    override fun getSpannable(): SpannableString {
        return mSpannable ?: SpannableString("")
    }

    private fun isDebug() = BuildConfig.DEBUG
}