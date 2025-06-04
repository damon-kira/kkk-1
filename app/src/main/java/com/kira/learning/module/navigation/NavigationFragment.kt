package com.kira.learning.module.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.helper.FragmentHelper
import com.common.lib.viewbinding.binding
import com.hjq.window.EasyWindow
import com.hjq.window.OnWindowViewClickListener
import com.hjq.window.draggable.MovingWindowDraggableRule
import com.kira.learning.R
import com.kira.learning.databinding.FragmentBlackBinding
import com.kira.learning.manager.Launch
import com.kira.learning.module.home.BaseHomeFragment
import com.kira.learning.module.home.MainActivity
import com.kira.learning.module.home.vm.HomeLoanViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * 样例代码导航页
 */
@AndroidEntryPoint
class NavigationFragment : BaseHomeFragment() {

    private val mBinding by binding(FragmentBlackBinding::inflate)

    private val mHomeViewModel by lazyActivityViewModel<HomeLoanViewModel>()

    private var mOrderIds: ArrayList<String>? = null

    private var mWindow: EasyWindow<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomListener(mBinding.toolbar)
//        mBinding.includeBlack.let {
//            it.tvText1.setText(R.string.black_text1)
//            it.tvText2.setText(R.string.refused_days)
//            it.tvDays.text = "0"
//            it.tvDesc.setText(R.string.black_desc)
//        }


//        mBinding.inclueRepay.tvBtn.setBlockingOnClickListener{
//            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_REPAY))
//        }

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {
//            mBinding.includeBlack.tvAmount.text = getString(R.string.days, it.WTvE5G.toString())

            val data = it.gQ1J
            if (data == null || data.isEmpty()) return@observe

//            mBinding.inclueRepay.llContent.show()
//            mBinding.inclueRepay.tvOrder.text = getString(R.string.orders, data.AMGH9kXswv)
//            mBinding.inclueRepay.tvAmount.text = getUnitString(data.RPBJ47rhC.orEmpty())
            mOrderIds = data.QLPGXTNU
        }
        onBackListener()
        initViewSetting()

        initWindow()
    }

    private fun initWindow() {
        mWindow = EasyWindow.with(getBaseActivity()!!)
            .setContentView(R.layout.window_hint)
//                    .setAnimStyle(R.style.IOSAnimStyle)
//                    .setImageDrawableByImageView(R.id.icon, R.drawable.ic_dialog_tip_finish)
//                    .setTextByTextView(R.id.message, "点我消失") // 设置成可拖拽的
            .setWindowDraggableRule(MovingWindowDraggableRule())
            .setOnClickListenerByView(
                R.id.win_tv_message,
                object : OnWindowViewClickListener<TextView> {
                    override fun onClick(
                        easyWindow: EasyWindow<*>,
                        view: TextView
                    ) {

//                                cancelAndRecycleEasyWindow(easyWindow)
                        FragmentHelper.getCurrFragment(
                            getMainActivity()?.supportFragmentManager ?: parentFragmentManager,
                            R.id.fl_main_container
                        )?.let { fragment ->
//                                    if (fragment is NavigationFragment) {
//                                        fragment.onRefresh()
//                                    }
                            Log.e(TAG, "onClick: 当前Fragment= ${FragmentHelper.getTag(fragment)}")
                        }
                    }
                })
    }

    private fun getMainActivity() = activity as MainActivity?

    fun onBackListener() {
        mBinding.toolbar.setOnClickListener {
            getMainActivity()?.drawerToggle()
        }
    }

    private fun initViewSetting() {
        mBinding.inclueDemoLayout1.let {
            it.tvDemoName.text = "拍照识别"
            it.tvBtn.setOnClickListener {
                Launch.skipPhotographActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout2.let {
            it.tvDemoName.text = "题目"
            it.tvBtn.setBlockingOnClickListener {
                Launch.skipAnswerActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout3.let {
            it.tvDemoName.text = "python编辑-运行"
            it.tvBtn.setOnClickListener {
                Launch.skipCodingActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout4.let {
            it.tvDemoName.text = "AI对话"
            it.tvBtn.setOnClickListener {
                Launch.skipAIIMActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout5.let {
            it.tvDemoName.text = "数据库"
            it.tvBtn.setOnClickListener {
                Launch.skipChatActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout6.let {
            it.tvDemoName.text = "视频"
            it.tvBtn.setOnClickListener {
                Launch.skipPlayerManageActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout7.let {
            it.tvDemoName.text = "测验"
            it.tvBtn.setOnClickListener {
//                Log.d("Language", "当前语言: ${Locale.getDefault().language}")
//                Log.d("Language", "完整地区: ${Locale.getDefault()}")
//                activity?.recreate()
                Launch.skipQuizActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout8.let {
            it.tvDemoName.text = "上传"
            it.tvBtn.setOnClickListener {
                Launch.skipUploadActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout9.let {
            it.tvDemoName.text = "总线通信"
            it.tvBtn.setOnClickListener {
//                setLogout()
//                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_LOGOUT))
//                LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
//                finish()
            }
        }
        mBinding.inclueDemoLayout10.let {
            it.tvDemoName.text = "悬浮按钮"
            it.tvBtn.setOnClickListener {
                if (mWindow?.isShowing == true) {
                    cancelAndRecycleEasyWindow(mWindow)
                } else {
                    mWindow?.show()
                }
            }
        }
    }

    private fun cancelAndRecycleEasyWindow(easyWindow: EasyWindow<*>?) {
        // 两种方式取消弹窗：
        // 1. easyWindow.cancel：顾名思义，取消显示
        // 2. easyWindow.recycle：在取消显示的基础上，加上了回收
        // 这两种区别在于，cancel 之后还能 show，但是 recycle 之后不能再 show
        // 通常情况下，如果你创建的 EasyWindow 对象在 cancel 之后永远不会再显示，取消弹窗建议直接用 recycle 方法，否则用 cancel 方法
        easyWindow?.cancel()
    }

    override fun onDestroyView() {
        mWindow?.recycle()
        super.onDestroyView()
    }


}