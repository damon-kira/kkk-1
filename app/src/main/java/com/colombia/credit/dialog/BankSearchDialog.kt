package com.colombia.credit.dialog

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.BankInfoSearch
import com.colombia.credit.databinding.DialogBankSelectorBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BankItemDecoration
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SearchAdapter
import com.colombia.credit.module.adapter.linearLayoutManager
import com.colombia.credit.view.SearchView
import com.common.lib.base.BaseActivity
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.hideSoftInput
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding

class BankSearchDialog(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogBankSelectorBinding>()

    private val mItems: ArrayList<BankInfoSearch> = arrayListOf()
    private val mAdapter: BankAdapter by lazy {
        BankAdapter(arrayListOf())
    }

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, MATCH, true)
        addTestData()
        mBinding.dialogBankSearchview.setHintText(R.string.bank_name_search_hint)
        mBinding.dialogBankRecyclerview.linearLayoutManager()
        mBinding.dialogBankRecyclerview.adapter = mAdapter
        mBinding.dialogBankRecyclerview.setOnItemClickListener(object :
            SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                mListener?.invoke(mItems[position])
                dismiss()
            }
        })
        mBinding.dialogBankRecyclerview.addItemDecoration(BankItemDecoration(context, mItems, true))
        mBinding.dialogBankSearchview.setOnSearchListener(object : SearchView.OnSearchViewListener {
            override fun onSearchTextChanged(searchText: String) {
                if (searchText.isEmpty()) {
                    mAdapter.setItems(mItems)
                } else {
                    mAdapter.filter.filter(searchText)
                }
            }
        })

        mBinding.dialogBankAivClose.setBlockingOnClickListener {
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        val ctx = context
        if (ctx is ContextThemeWrapper) {
            hideSoftInput((ctx.baseContext as BaseActivity))
        } else if (ctx is BaseActivity) {
            hideSoftInput(ctx)
        }
    }

    private var mListener: ((BankInfoSearch) -> Unit)? = null

    fun setData(bankInfo: ArrayList<BankInfoSearch>): BankSearchDialog {
//        mItems.clear()
//        mItems.addAll(bankInfo)
//        mAdapter.notifyDataSetChanged()
        return this
    }

    fun setOnClickListener(listener: (BankInfoSearch) -> Unit): BankSearchDialog {
        this.mListener = listener
        return this
    }

    class BankAdapter(items: ArrayList<BankInfoSearch>) :
        SearchAdapter<BankInfoSearch>(items, R.layout.layout_bank_name_item) {
        override fun convert(holder: BaseViewHolder, item: BankInfoSearch, position: Int) {
            holder.setText(R.id.tv_text, item.BName)
        }
    }

    private fun addTestData() {
        mItems.add(BankInfoSearch().also {
            it.BName = "bYYdddHFJHFJdfdfd"
            it.isCommonlyUsed = 1
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "CYYHFJHFJdfd"
            it.isCommonlyUsed = 1
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "BANK BRI"
            it.isCommonlyUsed = 1
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "BANK  BNI"
            it.isCommonlyUsed = 1
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "AYYHFJHFJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "AYYHFJHFdddJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "bYYHFJHFJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "bYYdddHFJHFJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "CYYHFJHFJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "DYYHFJHFJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "EYYHFJHFJ"
        })
        mItems.add(BankInfoSearch().also {
            it.BName = "GYYHFJHFJ"
        })
        mAdapter.setItems(mItems)
    }
}