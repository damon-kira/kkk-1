package com.colombia.credit.module.repeat.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.Launch
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatConfirmProductInfo
import com.colombia.credit.databinding.ActivityRepeatConfirmBinding
import com.colombia.credit.databinding.LayoutRepeatItemProductBinding
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.module.adapter.SafeLinearLayoutManager
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepeatConfirmActivity : BaseActivity(), View.OnClickListener {

    private val mBinding by binding<ActivityRepeatConfirmBinding>()

    private val mAdapter by lazy {
        ConfirmProductAdapter(arrayListOf())
    }

    private var mTotalAmount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setAdapter()
        mBinding.aivArrow.setBlockingOnClickListener(this)
        mBinding.tvConfirm.setBlockingOnClickListener(this)
        mBinding.tvCancel.setBlockingOnClickListener(this)
        mBinding.tvBankNo.setBlockingOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.tv_confirm -> {

            }
            R.id.tv_cancel -> {

            }
            R.id.aiv_arrow -> {
                // 底部list展开或收起
            }
            R.id.tv_bank_no -> {
                Launch.skipBankCardListActivity(this, mTotalAmount)
            }
        }
    }

    private fun setAdapter() {
        mBinding.recyclerview.layoutManager =
            SafeLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerview.adapter = mAdapter
        mBinding.recyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                // 计算还款信息
                mAdapter.notifyItemChanged(position)
            }
        })
    }

    private fun addHorizontalProduct(products: ArrayList<RepeatConfirmProductInfo>) {
        // 最多添加4个
        products.take(4).reversed().forEach { product ->
            val binding = LayoutRepeatItemProductBinding.inflate(
                LayoutInflater.from(this),
                mBinding.llProductList,
                false
            )
            val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f
            mBinding.llProductList.addView(binding.root, 0, layoutParams)
            // 设置产品名称及icon
        }
    }

}