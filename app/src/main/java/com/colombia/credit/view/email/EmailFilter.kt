package com.colombia.credit.view.email

import android.widget.Filter

class EmailFilter constructor(private val emailList: ArrayList<String>?): Filter() {

    private var mListener: IFilterResultListener? = null

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()
        if (constraint == null || constraint.isEmpty()) {
            results.values = ArrayList<EmailInputBean>()
            results.count = 0
        } else {
            val inputText = constraint.toString()
            val newList = ArrayList<EmailInputBean>()
            val lastIndex = inputText.lastIndexOf("@")
            var emailInputBean: EmailInputBean
            val sb = StringBuilder()
            if (lastIndex == -1) {
                val list = emailList
                if (list != null) {
                    for (s in list) {
                        emailInputBean = EmailInputBean(
                            inputText,
                            sb.append(getSubText(inputText)).append(s).toString()
                        )
                        newList.add(emailInputBean)
                        sb.delete(0, sb.length)
                    }
                }
            } else {
                val text = inputText.substring(lastIndex)
                val list = emailList
                if (list != null) {
                    for (s in list) {
                        if (s.startsWith(text)) {
                            emailInputBean = EmailInputBean(
                                inputText,
                                sb.append(getSubText(inputText)).append(s).toString()
                            )
                            newList.add(emailInputBean)
                            sb.delete(0, sb.length)
                        }
                    }
                }
            }
            results.values = newList
            results.count = newList.size
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        if (mListener != null) {
            val list = ArrayList<EmailInputBean>()
            try {
                if (results != null && results.count > 0) { //有符合过滤规则的数据
                    val tempList = results.values as? ArrayList<EmailInputBean> ?:return
                    list.addAll(tempList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mListener?.filterResult(constraint, list)
        }
    }


    private fun getSubText(text: String): String? {
        val indexOf = text.indexOf("@")
        return if (indexOf == -1) {
            text
        } else text.substring(0, indexOf)
    }

    interface IFilterResultListener {
        fun filterResult(constraint: CharSequence?, results: ArrayList<EmailInputBean>?)
    }

    fun setIFilterResultListener(listener: IFilterResultListener?) {
        mListener = listener
    }
}