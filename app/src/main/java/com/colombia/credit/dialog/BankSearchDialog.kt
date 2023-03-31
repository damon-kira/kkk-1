package com.colombia.credit.dialog

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.SearchInfo
import com.colombia.credit.bean.resp.BankInfoSearch
import com.colombia.credit.databinding.DialogBankSelectorBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BankItemDecoration
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SearchAdapter
import com.colombia.credit.view.SearchView
import com.common.lib.dialog.DefaultDialog
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
        setDisplaySize(MATCH, 0.7f, true)
        addTestData()
        mBinding.dialogBankRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.dialogBankRecyclerview.adapter = mAdapter
        mBinding.dialogBankRecyclerview.setOnItemClickListener(object :
            SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

            }
        })

        mBinding.dialogBankRecyclerview.addItemDecoration(
            BankItemDecoration(context, mItems, false),
            0
        )

        mBinding.dialogBankSearchview.setOnSearchListener(object : SearchView.OnSearchViewListener {
            override fun onSearchTextChanged(searchText: String) {
                if(searchText.isEmpty()) {
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

    private fun addTestData() {
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
            it.isCommonlyUsed = 1
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

    private var mListener: (() -> Unit)? = null

    private fun setOnClickListener(listener: () -> Unit): BankSearchDialog {
        this.mListener = listener
        return this
    }

    class BankAdapter(items: ArrayList<BankInfoSearch>) :
        SearchAdapter<BankInfoSearch>(items, R.layout.layout_bank_name_item) {
        override fun convert(holder: BaseViewHolder, item: BankInfoSearch, position: Int) {
            holder.setText(R.id.tv_text, item.BName)
        }
    }
}