package com.colombia.credit.dialog

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.databinding.DialogContactRelationshipBinding
import com.colombia.credit.expand.OnItemClickListener
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.BaseRecyclerViewAdapter
import com.colombia.credit.module.adapter.BaseViewHolder
import com.colombia.credit.module.adapter.SafeLinearLayoutManager
import com.common.lib.dialog.DefaultDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding


class ContactRelationShipDialog constructor(context: Context) : DefaultDialog(context) {

    private val mBinding by binding<DialogContactRelationshipBinding>()

    init {
        setContentView(mBinding.root)
        mBinding.aivClose.setBlockingOnClickListener {
            dismiss()
        }

        val mRelationShip = context.resources.getStringArray(R.array.contact_relationship)
        mBinding.relationshipRecyclerview.apply {
            layoutManager = SafeLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val list = mRelationShip.toList() as ArrayList<String>
            adapter = object :
                BaseRecyclerViewAdapter<String>(list, R.layout.layout_contact_relationship) {
                override fun convert(holder: BaseViewHolder, item: String, position: Int) {
                    holder.setText(R.id.relationship_tv_text, item)
                }
            }
            mBinding.relationshipRecyclerview.setOnItemClickListener(object :
                SimpleOnItemClickListener() {
                override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                    val item = mRelationShip[position]
                    mListener?.invoke(item)
                }
            })
        }
    }

    private var mListener: ((String) -> Unit)? = null

    fun setOnClickListener(listener: (String) -> Unit): ContactRelationShipDialog {
        this.mListener = listener
        return this
    }
}