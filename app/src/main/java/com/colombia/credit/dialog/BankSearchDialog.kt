package com.colombia.credit.dialog

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.BankInfoSearch
import com.colombia.credit.databinding.DialogBankSelectorBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SearchAdapter
import com.common.lib.dialog.DefaultDialog
import com.common.lib.viewbinding.binding

class BankSearchDialog(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogBankSelectorBinding>()

    private val mItems: ArrayList<BankInfoSearch> = arrayListOf()
    private val mAdapter: BankAdapter by lazy {
        BankAdapter(mItems)
    }

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, 0.7f)

        mBinding.dialogBankRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.dialogBankRecyclerview.adapter = mAdapter
        mBinding.dialogBankRecyclerview.setOnItemClickListener(object :
            SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

            }
        })
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