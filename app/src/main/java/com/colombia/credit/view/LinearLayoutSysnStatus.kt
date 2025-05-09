package com.colombia.credit.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout


class LinearLayoutSysnStatus : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        val childCount = childCount
        for (index in 0 until childCount) {
            getChildAt(index).isSelected = selected
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        val childCount = childCount
        for (index in 0 until childCount) {
            getChildAt(index).isEnabled = enabled
        }
    }
}