package com.colombia.credit.manager

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.colombia.credit.expand.isValidChar


object InputHelper {

    open class TextWatchAdapter : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
    }


    /**
     * 初始化输入框过滤器
     */
    fun createFilter(max: Int, infilter: InputFilter?): Array<InputFilter> {
        val filters = arrayListOf<InputFilter>()
        filters.add(InputFilter.LengthFilter(max))
        if (infilter != null) {
            filters.add(infilter)
        }
        return filters.toTypedArray()
    }


    val idFilter = InputFilter { source, start, end, dest, dstart, dend ->
        if (isValidChar(source.toString().replace(" ".toRegex(), ""))) {
            return@InputFilter source
        } else {
            return@InputFilter ""
        }
    }
}