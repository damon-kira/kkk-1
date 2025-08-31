package com.kira.learning.dialog

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.kira.learning.R
import com.kira.learning.bean.res.RspBankNameInfo
import com.kira.learning.databinding.DialogBankSelectorBinding
import com.kira.learning.expand.SimpleOnItemClickListener
import com.kira.learning.expand.setOnItemClickListener
import com.kira.learning.xml.adapter.BankItemDecoration
import com.kira.learning.xml.adapter.BaseViewHolder
import com.kira.learning.xml.adapter.SearchAdapter
import com.kira.learning.xml.adapter.linearLayoutManager
import com.kira.learning.view.SearchView
import com.common.lib.base.BaseActivity
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.expand.hideSoftInput

class BankSearchDialog(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogBankSelectorBinding>()

    private val mItems: ArrayList<RspBankNameInfo.BankNameInfo> = arrayListOf()
    private val mAdapter: BankAdapter by lazy {
        BankAdapter(arrayListOf())
    }

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, MATCH, true)
        mBinding.dialogBankSearchview.setHintText(R.string.bank_name_search_hint)
        mBinding.dialogBankRecyclerview.linearLayoutManager()
        mBinding.dialogBankRecyclerview.adapter = mAdapter
        mBinding.dialogBankRecyclerview.setOnItemClickListener(object :
            SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                mAdapter.getItemData<RspBankNameInfo.BankNameInfo>(position)?.let {
                    mListener?.invoke(it)
                }
                mBinding.dialogBankSearchview.clearSearchText()
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

    private var mListener: ((RspBankNameInfo.BankNameInfo) -> Unit)? = null

    fun setData(bankInfo: ArrayList<RspBankNameInfo.BankNameInfo>): BankSearchDialog {
        mItems.clear()
        mItems.addAll(bankInfo)
        mAdapter.setItems(mItems)
        return this
    }

    fun setOnSelectListener(listener: (RspBankNameInfo.BankNameInfo) -> Unit): BankSearchDialog {
        this.mListener = listener
        return this
    }

    class BankAdapter(items: ArrayList<RspBankNameInfo.BankNameInfo>) :
        SearchAdapter<RspBankNameInfo.BankNameInfo>(items, R.layout.layout_bank_name_item) {
        override fun convert(holder: BaseViewHolder, item: RspBankNameInfo.BankNameInfo, position: Int) {
            holder.setText(R.id.tv_text, item.KoGUgumBVm)
        }
    }
}