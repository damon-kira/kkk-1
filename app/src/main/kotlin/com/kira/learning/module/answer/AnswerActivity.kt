package com.kira.learning.module.answer

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.lib.base.BaseActivity
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.kira.learning.R
import com.kira.learning.bean.QuestionProcessInfo
import com.kira.learning.databinding.ActivityAnswerBinding
import com.kira.learning.expand.showCustomDialog
import com.kira.learning.module.answer.adapter.AnswerAdapter
import com.kira.learning.module.answer.vm.AnswerViewModel
import com.kira.learning.view.ToolbarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnswerActivity : BaseActivity() {

    private val mBinding by binding<ActivityAnswerBinding>()
    private val mViewModel by lazyViewModel<AnswerViewModel>()

    private var chatMessages: ArrayList<QuestionProcessInfo> = ArrayList<QuestionProcessInfo>()
    lateinit var mAdapter: AnswerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setViewModelLoading(mViewModel)
        initObserver()

        initViewSetting()

        searchQuestion()
    }

    private fun initViewSetting() {
        setToolbarListener(mBinding.processToolbar)
        mBinding.processToolbar.setRightImage(R.drawable.ic_birthday)
        mAdapter = AnswerAdapter(chatMessages, mBinding.answerRecyclerView).also {
            it.setHasStableIds(true)
        }
        mBinding.answerRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.answerRecyclerView.adapter = mAdapter
//        val isExpanded = expandable_group.visibility == View.VISIBLE
//        expandable_group.visibility = if (isExpanded) View.GONE else View.VISIBLE
//        // 添加动画（可选）
//        TransitionManager.beginDelayedTransition(parent_layout, AutoTransition())
    }

    private fun searchQuestion() {
        lifecycleScope.launch {
            mViewModel.searchQuestion("")
        }
    }

    private fun initObserver() {
        mViewModel.datas.observerNonSticky(this) {
            if (it.isSuccess()) {
                it.getData()?.let { data ->
                    // Success
                    val responseData = data
                    Log.e(TAG, "initObserver: 请求成功 ${responseData}")

                    chatMessages.add(responseData)
                    chatMessages.add(responseData)

                    // mAdapter.no tifyDataSetChanged();
                    mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1)
                    mBinding.answerRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1)
                }
            }
        }

        // 观察数据变化
//        mViewModel.responses.observe(this) { responses ->
//            // 更新 UI（例如显示在 RecyclerView）
//        }
    }

    override fun onDestroy() {
//        LiveDataBus.removeObserve(PayEvent::class.kotlin, mObserver)
        super.onDestroy()
    }

    fun setToolbarListener(toolbarLayout: ToolbarLayout) {
        toolbarLayout.setOnbackListener {
            onBackPressed()
        }
        toolbarLayout.setCustomClickListener {
            showCustomDialog()
        }
    }

}