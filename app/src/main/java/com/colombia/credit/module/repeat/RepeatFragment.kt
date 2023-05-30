package com.colombia.credit.module.repeat

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colombia.credit.R
import com.colombia.credit.bean.resp.RepeatProductInfo
import com.colombia.credit.databinding.FragmentRepeatBinding
import com.colombia.credit.dialog.RecommendDialog
import com.colombia.credit.expand.SimpleOnItemClickListener
import com.colombia.credit.expand.getUnitString
import com.colombia.credit.expand.setOnItemClickListener
import com.colombia.credit.expand.toast
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.adapter.SpaceItemDecoration
import com.colombia.credit.module.home.BaseHomeLoanFragment
import com.colombia.credit.module.home.HomeLoanViewModel
import com.colombia.credit.module.home.MainEvent
import com.colombia.credit.permission.HintDialog
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.dp
import com.util.lib.hide
import com.util.lib.ifShow
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.min


//复贷首页
@AndroidEntryPoint
class RepeatFragment : BaseHomeLoanFragment() {

    private val mBinding by binding(FragmentRepeatBinding::inflate)

    private val mAdapter: RepeatProductAdapter by lazy {
        RepeatProductAdapter(arrayListOf(), mBinding.repeatRecyclerview)
    }

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private var mOrderIds: String? = null

    private val mRecommHelper by lazy {
        RecommHelper().also {
            it.mCountDownLivedata.observerNonSticky(viewLifecycleOwner) {
                showRecommend()
            }
        }
    }

    private val mRecommendDialog by lazy {
        RecommendDialog(getSupportContext()).also {
            it.setClickListener { info ->
                it.dismiss()
                Launch.skipRepeatConfirmActivity(getSupportContext(), info.eqOEs.orEmpty())
            }
            it.setOnDismissListener {
                mRecommHelper.reset()
            }
        }
    }

    private val mHintDialog by lazy {
        HintDialog(getSupportContext()).also {
            it.showClose(true)
                .setTitleText(getString(R.string.repeat_error_title))
                .setIcon(R.drawable.ic_error_image)
                .setBtnText(getString(R.string.kyc_dialog_btn))
        }
    }

    override fun contentView(): View = mBinding.root

    override fun onPullToRefresh() {
        mHomeViewModel.getHomeInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)

        viewLifecycleOwner.lifecycle.addObserver(mRecommHelper)
        mBinding.groupBtn.referencedIds = intArrayOf(R.id.etv_tag, R.id.repeat_tv_apply)
        setOffset()
        initView(view)
        initObserver()
    }

    override fun onTouchEvent(ev: MotionEvent?) {
        if (ev?.action == MotionEvent.ACTION_UP || ev?.action == MotionEvent.ACTION_CANCEL || ev?.action == MotionEvent.ACTION_POINTER_UP) {
            mRecommHelper.startCountDown()
        } else mRecommHelper.cancel()
    }

    private fun initView(view: View) {
        initRecyclerview(view)
        mBinding.clContent.post {
            val maxHeight = mBinding.clContent.height - mBinding.toolbar.bottom - 30f.dp()
            mAdapter.setEmptyMaxHeight(maxHeight)
        }
        mBinding.repeatTvApply.setBlockingOnClickListener {
            val leftNum = mHomeViewModel.mRspInfoLiveData.value?.GbiDSBdW ?: 0
            val list = mAdapter.getSelectorItems().map { it.eqOEs }
            var orderIds: String? = null
            if (list.isEmpty()) { // 没有选择产品时，获取待确认订单第一个
                if (mAdapter.getWaitItemCount() > 0){
                    orderIds = mAdapter.getWaitItemData(0)?.tQXtG0FYb.orEmpty()
                }
            } else if (leftNum < list.size || leftNum == 0) {
                mHintDialog.setOnClickListener {
                    mAdapter.getSelectorItems().forEach { item ->
                        item.change()
                    }
                    mAdapter.notifyDataSetChanged()
                }.setMessage(getString(R.string.toast_product_app))
                getBaseActivity()?.addDialog(mHintDialog)
                return@setBlockingOnClickListener
            }
            Launch.skipRepeatConfirmActivity(getSupportContext(), list.joinToString(","), orderIds)
        }

        mBinding.includeOrders.tvBtn.setBlockingOnClickListener {
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
        }
    }

    private fun initRecyclerview(view: View) {
        mBinding.repeatRecyclerview.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        mBinding.repeatRecyclerview.adapter = mAdapter
        mBinding.repeatRecyclerview.addItemDecoration(
            SpaceItemDecoration(
                SpaceItemDecoration.VERTICAL_LIST, 12f.dp()
            )
        )
        mBinding.repeatRecyclerview.itemAnimator?.changeDuration = 0

        mBinding.repeatRecyclerview.setOnItemClickListener(object : SimpleOnItemClickListener() {
            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
                val viewType = mAdapter.getItemViewType(position)
                if (viewType == RepeatProductAdapter.TYPE_WAIT) {
                    val data = mAdapter.getWaitItemData(position)
                    Launch.skipRepeatConfirmActivity(
                        getSupportContext(),
                        "",
                        orderIds = data?.tQXtG0FYb.orEmpty()
                    )
                } else if (viewType == RepeatProductAdapter.TYPE_NORMAL) {
                    val finalPosi = mAdapter.getNormalItemPosition(position)
                    val info = mHomeViewModel.mRspInfoLiveData.value
                    val maxNum = info?.A04fSYQdHM ?: 0
                    val item = mAdapter.getItemData<RepeatProductInfo>(finalPosi)
                    if (item != null && !item.selector() && maxNum <= mAdapter.getSelectorItems().size && mAdapter.getWaitItemCount() == 0) {
                        if (maxNum > 0) {
                            mHintDialog.setMessage(
                                getString(
                                    R.string.toast_product_up,
                                    maxNum.toString()
                                )
                            ).setOnClickListener { }
                            getBaseActivity()?.addDialog(mHintDialog)
                        }
                        return
                    }

                    val leftNum = info?.GbiDSBdW ?: 0 // 最大可申请笔数
                    if (item != null && item.selector() && mAdapter.getSelectorItems().size == 1 && leftNum > 0 && mAdapter.getWaitItemCount() == 0) {
                        toast(R.string.toast_min_product)
                    } else {
                        item?.change()
                        mAdapter.notifyItemChanged(position)
                    }

                }
            }
        })
    }

    private fun initObserver() {
        mHomeViewModel.repeatProductLiveData.observe(viewLifecycleOwner) {
            mAdapter.setItems(it)
            val params = it?.firstOrNull()?.g7tzi ?: "0"
            mBinding.groupBtn.ifShow(!it.isNullOrEmpty())
            mBinding.etvTag.text = getString(R.string.hosta_s, getUnitString(params))
            mRecommHelper.reset()
        }

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
            mRecommHelper.setCountDownTime(it.swOwF0KJ)
            mRecommHelper.reset()
            mAdapter.setWaitItems(it.Jg4g2)
            setOffset()
            val data = it.gQ1J
            if (data == null || data.isEmpty()) {
                mBinding.includeOrders.llContent.hide()
                mOrderIds = null
                return@observe
            }
            mBinding.includeOrders.llContent.show()
            mBinding.includeOrders.tvOrder.text = getString(R.string.orders, data.AMGH9kXswv)
            mBinding.includeOrders.tvAmount.text = getUnitString(data.RPBJ47rhC.orEmpty())
            mOrderIds = data.QLPGXTNU?.joinToString(",")
        }

        mHomeViewModel.waitConfirmLiveData.observe(viewLifecycleOwner) {
            if (it == null || it.isEmpty()) {
                mBinding.includeWait.root.hide()
                return@observe
            }
        }
    }

    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if(visible) {
            onPullToRefresh()
            mRecommHelper.reset ()
        } else {
            mRecommHelper.cancel()
        }
    }

    private fun setOffset() {
        if (mAdapter.itemCount < 3) {
            changeListPadding(0)
            return
        }

        if (mBinding.etvTag.top > 0) {
            changeListPadding(mBinding.etvTag.top)
        } else {
            mBinding.etvTag.post {
                if (isDestroyView()) return@post
                changeListPadding(mBinding.etvTag.top)
            }
        }
    }

    private fun changeListPadding(offset: Int) {
        val padding = if (offset == 0) {
            0
        } else mBinding.clContent.height - offset
        mBinding.repeatRecyclerview.setPadding(
            mBinding.repeatRecyclerview.paddingLeft,
            mBinding.repeatRecyclerview.paddingTop,
            mBinding.repeatRecyclerview.paddingRight,
            padding
        )
    }

    private fun showRecommend() {
        if (mRecommendDialog.isShowing || !isFragmentVisible()) return
        val count = min(3, mAdapter.getNormalItemCount())
        if (count == 0) return
        val index = (Math.random() * 100).toInt() % count
        val item = mAdapter.getItemData<RepeatProductInfo>(index)
        mRecommendDialog.setInfo(item)
        getBaseActivity()?.addDialog(mRecommendDialog)
    }
}