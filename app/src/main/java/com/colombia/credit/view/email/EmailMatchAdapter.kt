package com.colombia.credit.view.email

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.StringUtils
import com.util.lib.dp
import com.util.lib.sp


/**
 * Created by weisl on 2019/4/4.
 */
class EmailMatchAdapter(items: ArrayList<String>) : BaseAdapter(), Filterable,
    EmailFilter.IFilterResultListener {

    private val mSearchDataBaseList = mutableListOf<String>()

    private val mSearchResultList = mutableListOf<EmailInputBean>()

    private var mFilter: EmailFilter? = null

    init {
        mSearchDataBaseList.addAll(items)
        val list = arrayListOf<String>()
        list.addAll(mSearchDataBaseList)
        mFilter = EmailFilter(list)
        mFilter?.setIFilterResultListener(this)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: ViewHolder? = null
        var view = convertView
        if (view != null) {
            holder = view.tag as ViewHolder?
        } else {
            view = TextView(parent.context).also {
                val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                it.setTextColor(ContextCompat.getColor(parent.context, R.color.colorPrimary))
                it.textSize = 6.dp()
                val padding2 = 2f.dp()
                it.setPadding(6f.dp(), padding2, padding2, padding2)
                it.layoutParams = params
            }
            holder = ViewHolder(view)
            view.tag = holder
        }
        val emailInputBean = mSearchResultList[position]
        val span = StringUtils.getSpannableString(
            parent.context,
            "${emailInputBean.email}",
            R.color.color_999999,
            emailInputBean.inputText
        )
        holder?.tv?.text = span
        return view!!
    }

    override fun getItem(position: Int): String {
        return mSearchResultList[position].email ?: ""
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mSearchResultList.size
    }

    override fun getFilter(): Filter {
        var tempFilter = mFilter
        if (tempFilter == null) {
            val list = arrayListOf<String>()
            tempFilter = EmailFilter(list)
            mFilter = tempFilter
            mFilter?.setIFilterResultListener(this)
        }
        return tempFilter
    }

    override fun filterResult(
        constraint: CharSequence?,
        results: java.util.ArrayList<EmailInputBean>?
    ) {
        if (results != null && results.size > 0) {//有符合过滤规则的数据
            mSearchResultList.clear()
            mSearchResultList.addAll(results)
            notifyDataSetChanged()
        } else {//没有符合过滤规则的数据
            notifyDataSetInvalidated()
        }
    }

    private fun getSubText(text: String): String {
        val indexOf = text.indexOf("@")
        if (indexOf == -1) {
            return text
        }
        return text.substring(0, indexOf)
    }

    class ViewHolder(view: TextView) {
        var tv: TextView? = null

        init {
            tv = view
        }
    }
}