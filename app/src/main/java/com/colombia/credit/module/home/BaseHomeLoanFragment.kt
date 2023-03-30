package com.colombia.credit.module.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.colombia.credit.R
import com.colombia.credit.databinding.FragmentBaseLoanBinding
import com.common.lib.viewbinding.binding
import com.util.lib.dp

abstract class BaseHomeLoanFragment: BaseHomeFragment() {

    private val mBinding by binding(FragmentBaseLoanBinding::inflate)

    private val mScrollView: NestedScrollView by lazy {
        NestedScrollView(getSupportContext()).apply {
            isFillViewport = true
            overScrollMode = View.OVER_SCROLL_NEVER
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contentView = contentView()
        if (contentView.parent == null) {
            mBinding.swipeRefresh.setProgressViewOffset(false, 50f.dp(), 100f.dp())
            if (needScrollView()) {
                mBinding.swipeRefresh.addView(mScrollView)
                mScrollView.addView(contentView)
            } else {
                mBinding.swipeRefresh.addView(contentView)
            }
        }
        mBinding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getSupportContext(), R.color.colorPrimary))
        mBinding.swipeRefresh.setOnRefreshListener {
            onPullToRefresh()
        }
    }

    fun stopRefresh() {
        mBinding.swipeRefresh.run {
            if (isRefreshing) {
                isRefreshing = false
            }
        }
    }

    open fun needScrollView(): Boolean = true

    abstract fun contentView(): View

    abstract fun onPullToRefresh()
}