package com.colombia.credit.module.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.util.lib.log.logger_e


class SafeLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        try {//捕获数组越界
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            logger_e("MyLinearLayoutManager", "$e")
        }
    }
}

fun Context.linearLayoutManager() =
    SafeLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)