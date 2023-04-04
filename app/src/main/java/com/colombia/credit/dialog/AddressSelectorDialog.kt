package com.colombia.credit.dialog

import android.content.Context
import android.provider.Telephony.Mms.Addr
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.colombia.credit.R
import com.colombia.credit.bean.AddressInfo
import com.colombia.credit.databinding.DialogAddrSelectorBinding
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.MyDividerItemDecoration
import com.colombia.credit.module.adapter.linearLayoutManager
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.getContext

class AddressSelectorDialog(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogAddrSelectorBinding>()
    private val mItems = arrayListOf<AddressInfo>()
    private val mAdapter by lazy {
        AddressSearchAdapter(mItems)
    }

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, 0.65f, true)
        mBinding.aivClear.setBlockingOnClickListener {
            mBinding.addrEdittext.setText("")
        }
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }
        mBinding.aivSearch.setBlockingOnClickListener {
            val realText = mBinding.addrEdittext.getRealText()
            if (realText.isEmpty()) {
                return@setBlockingOnClickListener
            }
        }

        for (index in 0 until 60) {
            mItems.add(AddressInfo().also {
                it.address = "$index"
            })
        }
        mBinding.searchRecyclerview.linearLayoutManager()
        mBinding.searchRecyclerview.addItemDecoration(
            MyDividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        mBinding.searchRecyclerview.adapter = mAdapter
    }

    fun setAddressInfo(addressInfo: ArrayList<AddressInfo>): AddressSelectorDialog {
        mAdapter.setItems(addressInfo)
        return this
    }
}

class AddressSearchAdapter(items: ArrayList<AddressInfo>, private val curr: String = "") :
    BaseRecyclerViewAdapter<AddressInfo>(items, R.layout.layout_addr_search_item) {
    override fun convert(holder: BaseViewHolder, item: AddressInfo, position: Int) {
        holder.getView<TextView>(R.id.tv_text).let {
            it.text = item.address
            val isSelector = item.address == curr
            it.isSelected = isSelector
            if (isSelector) {
                it.setCompoundDrawables(
                    null,
                    null,
                    AppCompatResources.getDrawable(holder.getContext(), R.drawable.svg_coupler),
                    null
                )
            } else {
                it.setCompoundDrawables(
                    null,
                    null,
                    null,
                    null
                )
            }
        }
    }

}