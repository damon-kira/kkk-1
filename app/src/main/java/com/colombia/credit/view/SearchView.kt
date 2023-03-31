package com.colombia.credit.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.colombia.credit.databinding.LayoutSearchViewBinding
import com.common.lib.expand.setBlockingOnClickListener
import com.util.lib.ifShow


class SearchView : LinearLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private val binding: LayoutSearchViewBinding =
        LayoutSearchViewBinding.inflate(LayoutInflater.from(context), this)

    private fun initView(context: Context?, attrs: AttributeSet?) {
        orientation = HORIZONTAL
        binding.setSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    binding.aivClear.ifShow(s.toString().isNotEmpty())
                    listener?.onSearchTextChanged(it.toString())
                }
            }

        })
        binding.aivClear.setBlockingOnClickListener {
            binding.setSearch.setText("")
        }
    }

    fun getSearchText(): String = binding.setSearch.text.toString()

    var listener: OnSearchViewListener? = null

    fun setOnSearchListener(listener: OnSearchViewListener?) {
        this.listener = listener
    }


    interface OnSearchViewListener {

        fun onSearchTextChanged(searchText: String)
    }

}