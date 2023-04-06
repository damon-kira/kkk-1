package com.colombia.credit.dialog

import android.content.Context
import com.colombia.credit.databinding.DialogDatePickerBinding
import com.colombia.credit.view.WheelView
import com.common.lib.dialog.DefaultDialog
import com.common.lib.viewbinding.binding
import com.util.lib.log.logger_d
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class DatePickerDialog constructor(context: Context) : DefaultDialog(context) {

    private var mSelectClick: ((year: String, month: String, day: String) -> Unit)? = null

    private val mBinding by binding<DialogDatePickerBinding>()

    private val mDecimalFormat = DecimalFormat("#00")

    private val mCurrYear by lazy {
        Calendar.getInstance().get(Calendar.YEAR)
    }
    private val mYears by lazy {
        arrayListOf<String>().also {
            val currYear = mCurrYear
            val startYear = currYear - 18
            val endYear = currYear - 65
            for (index in startYear..endYear) {
                it.add(index.toString())
            }
        }
    }

    private val mMonth by lazy {
        arrayListOf<String>().also {
            for (index in 1..12) {
                it.add(mDecimalFormat.format(index))
            }
        }
    }

    private var mDays = arrayListOf<String>().also {
        for (index in 1..31) {
            it.add(mDecimalFormat.format(index))
        }
    }

    private val TAG = "DatePickerDialog"

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, 0.5f, true)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        mBinding.wlYear.updateItemList(mYears)
        mBinding.wlMonth.updateItemList(mMonth)
        mBinding.wlDay.updateItemList(mDays)

        mBinding.wlYear.onWheelViewListener = object : WheelView.OnWheelViewListener {
            override fun onSelected(pos: Int, item: String) {

            }
        }

        mBinding.wlMonth.onWheelViewListener = object : WheelView.OnWheelViewListener {
            override fun onSelected(pos: Int, item: String) {
                val year = mBinding.wlYear.getCurrItem()?.toInt() ?: 0
                val month = mBinding.wlMonth.getCurrItem()?.toInt() ?: 0
                if (year != 0 && month != 0) {
                    mBinding.wlDay.updateItemList(getDaysStr(getMonthDays(year, month)))
                }
            }
        }
        mBinding.wlDay.onWheelViewListener = object : WheelView.OnWheelViewListener {
            override fun onSelected(pos: Int, item: String) {

            }
        }
    }

    private fun getMonthDays(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, 1)
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE).also {
            logger_d(TAG, "days = $it")
        }
    }

    private fun getDaysStr(count: Int): ArrayList<String> {
        val list = arrayListOf<String>()
        for (index in 1..count) {
            list.add(mDecimalFormat.format(index))
        }
        return list
    }

    fun setOnSelectorClick(click: (year: String, month: String, day: String) -> Unit) {
        this.mSelectClick = click
    }
}