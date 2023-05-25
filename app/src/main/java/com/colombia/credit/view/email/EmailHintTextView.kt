package com.colombia.credit.view.email

import android.content.Context
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.util.lib.log.logger_d


class EmailHintTextView : AppCompatAutoCompleteTextView {

    private val mItems: ArrayList<String> = arrayListOf()

    private var mAdapter: EmailMatchAdapter? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    private fun init() {
        threshold = 1
        mAdapter = EmailMatchAdapter(mItems)
        setAdapter(mAdapter)
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
                mAdapter?.filter?.filter(text)
            }

        })
    }

    fun setListData(listData: ArrayList<String>) {
        mItems.clear()
        mItems.addAll(listData)
        init()
    }

    override fun setSelection(start: Int, stop: Int) {
        val spannableText = text
        if (spannableText.isEmpty())
            return
        try {
            Selection.setSelection(spannableText, start, stop)
        } catch (e: IndexOutOfBoundsException) {
            com.util.lib.log.debug {
                throw e
            }
        }
    }

    override fun setSelection(index: Int) {
        val spannableText = text
        if (spannableText.isNullOrEmpty())
            return
        try {
            Selection.setSelection(spannableText, index)
        } catch (e: IndexOutOfBoundsException) {
            com.util.lib.log.debug {
                throw e
            }
        }
    }
}