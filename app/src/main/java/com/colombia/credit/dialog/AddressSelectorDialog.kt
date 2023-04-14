package com.colombia.credit.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.AddressInfo
import com.colombia.credit.bean.SearchInfo
import com.colombia.credit.databinding.DialogAddrSelectorBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.*
import com.colombia.credit.view.SearchView
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.ifShow

class AddressSelectorDialog(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogAddrSelectorBinding>()
    private val mParentItems = arrayListOf<AddressInfo>()
    private var mCurrParent: String? = null

    private val mItemsCity = arrayListOf<AddressInfo.City>()
    private var mCurrCity: String? = null

    private val TYPE_PARENT = 1
    private val TYPE_CITY = 2
    private var currType = TYPE_PARENT

    private val mParentAdapter by lazy {
        AddressSearchAdapter(arrayListOf())
    }
    private val mCityAdapter by lazy {
        CitySearchAdapter(arrayListOf())
    }

    private var mListener: ((AddressInfo.City?) -> Unit)? = null

    init {
        setContentView(mBinding.root)
        setDisplaySize(MATCH, MATCH, true)

        mBinding.searchview.setOnSearchListener(object : SearchView.OnSearchViewListener {
            override fun onSearchTextChanged(searchText: String) {
                if (searchText.isNullOrEmpty()) {
                    if (currType == TYPE_CITY) {
                        mCityAdapter.setItems(mItemsCity)
                    } else {
                        mParentAdapter.setItems(mParentItems)
                    }
                } else {
                    if (currType == TYPE_CITY) {
                        mCityAdapter.filter.filter(searchText)
                    } else {
                        mParentAdapter.filter.filter(searchText)
                    }
                }
            }
        })

        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }

        val backListener = View.OnClickListener {
            if (currType == TYPE_CITY) {
                setCurrType(TYPE_PARENT)
                mBinding.searchview.clearSearchText()
                val list = arrayListOf<AddressInfo>()
                list.addAll(mParentItems)
                setAddressInfo(list)
            } else {
                dismiss()
            }
        }
        mBinding.aivBack.setBlockingOnClickListener(backListener)
        mBinding.tvFirst.setBlockingOnClickListener(backListener)

        mBinding.searchRecyclerview.linearLayoutManager()
        mBinding.searchRecyclerview.addItemDecoration(
            MyDividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        mBinding.searchRecyclerview.adapter = mParentAdapter

        mBinding.searchRecyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                if (currType == TYPE_PARENT) {
                    setCurrType(TYPE_CITY)
                    mBinding.searchview.clearSearchText()
                    mParentAdapter.getItemData<AddressInfo>(position)?.let { item ->
                        setCityInfo(item.sonList!!)
                        mCurrParent = item.cingorium
                        mBinding.tvFirst.text = item.cingorium
                    }
                } else {
                    val item = mCityAdapter.getItemData<AddressInfo.City>(position)
                    item?.isCheck = item?.isCheck != true
                    mCurrCity = item?.trophful
                    mListener?.invoke(item)
                    dismiss()
                }
            }
        })
    }

    private fun setCurrType(type: Int) {
        currType = type
        val isShow = type == TYPE_CITY
        mBinding.ivProcess.ifShow(isShow)
        mBinding.tvFirst.ifShow(isShow)
    }

    fun setAddressInfo(addressInfo: ArrayList<AddressInfo>): AddressSelectorDialog {
        setCurrType(TYPE_PARENT)
        mBinding.searchRecyclerview.adapter = mParentAdapter
        mParentAdapter.curr = mCurrParent.orEmpty()
        mParentItems.clear()
        mParentItems.addAll(addressInfo)
        mParentAdapter.setItems(addressInfo)
        return this
    }

    fun setCityInfo(citys: ArrayList<AddressInfo.City>): AddressSelectorDialog {
        setCurrType(TYPE_CITY)
        mBinding.searchRecyclerview.adapter = mCityAdapter
        mCityAdapter.curr = mCurrCity.orEmpty()
        mItemsCity.clear()
        mItemsCity.addAll(citys)
        mCityAdapter.setItems(citys)
        return this
    }

    // 省市使用英文逗号’,‘分割
    fun setSelectorListener(listener: (AddressInfo.City?) -> Unit): AddressSelectorDialog {
        this.mListener = listener
        return this
    }
}

open class AbsAddresAdater<T : SearchInfo>(items: ArrayList<T>, var curr: String = "") :
    SearchAdapter<T>(items, R.layout.layout_addr_search_item) {

    override fun convert(holder: BaseViewHolder, item: T, position: Int) {
        if (item is AddressInfo) {
            setText(holder, item.cingorium.orEmpty())
        } else if (item is AddressInfo.City) {
            setText(holder, item.trophful.orEmpty())
        }
    }

    private fun setText(
        holder: BaseViewHolder,
        text: String
    ) {
        holder.getView<TextView>(R.id.tv_text).let {
            it.text = text
            val isSelector = text == curr
            it.isSelected = isSelector
            val rightDrawable = if (isSelector) AppCompatResources.getDrawable(
                holder.getContext(),
                R.drawable.svg_coupler
            ) else null
            it.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
        }
    }
}

class AddressSearchAdapter(items: ArrayList<AddressInfo>, curr: String = "") :
    AbsAddresAdater<AddressInfo>(items, curr) {}

class CitySearchAdapter(items: ArrayList<AddressInfo.City>, curr: String = "") :
    AbsAddresAdater<AddressInfo.City>(items, curr) {
}