package com.kira.learning.module.navigation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.helper.FragmentHelper
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.hjq.window.EasyWindow
import com.hjq.window.OnWindowViewClickListener
import com.hjq.window.draggable.MovingWindowDraggableRule
import com.kira.learning.R
import com.kira.learning.databinding.FragmentBlackBinding
import com.kira.learning.expand.setLogout
import com.kira.learning.expand.showAppUpgradeDialog
import com.kira.learning.manager.Launch
import com.kira.learning.module.home.BaseHomeFragment
import com.kira.learning.module.home.HomeEvent
import com.kira.learning.module.home.MainActivity
import com.kira.learning.module.home.MainEvent
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

    private var mRemovableWindow: EasyWindow<*>? = null

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
        initToolbar()
        initViewSetting()

        initWindow()
    }

    private fun initWindow() {
        getBaseActivity() ?: return

        mRemovableWindow = EasyWindow.with(getBaseActivity()!!)
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

    private fun showCustomMenu(anchor: View) {
        val popup = PopupMenu(getBaseActivity()!!, anchor, Gravity.END)
        popup.menuInflater.inflate(R.menu.main_menu, popup.menu)

        // 强制显示图标
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }
        // 对于较旧版本的回退方案
        else {
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopup = field.get(popup)
                        val classPopup = Class.forName(menuPopup.javaClass.name)

                        // 尝试调用 setForceShowIcon 方法
                        try {
                            val setForceShowIcon = classPopup.getMethod(
                                "setForceShowIcon",
                                Boolean::class.javaPrimitiveType
                            )
                            setForceShowIcon.invoke(menuPopup, true)
                        }
                        // 如果方法不存在，尝试旧版方法
                        catch (e: NoSuchMethodException) {
                            val method = classPopup.getMethod(
                                "setForceShowIcon",
                                Boolean::class.javaPrimitiveType
                            )
                            method.invoke(menuPopup, true)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // 设置菜单项点击监听
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_settings -> {
//                    startActivity(Intent(this, SettingsActivity::class.java))
                    Log.e(TAG, "showCustomMenu: 点击了 action_settings")
                    true
                }

                R.id.action_help -> {
//                    shareContent()
                    Log.e(TAG, "showCustomMenu: 点击了 action_help")
                    true
                }

                R.id.action_logout -> {
//                    logout()
                    Log.e(TAG, "showCustomMenu: 点击了 action_logout")
                    true
                }

                else -> false
            }
        }

        // 显示菜单
        popup.show()
    }

    private fun getMainActivity() = activity as MainActivity?

    fun initToolbar() {
        mBinding.toolbar.setRightImage(R.drawable.svg_me_nor)
        mBinding.toolbar.setCustomImage(R.drawable.alive_anim_blink)
//        mBinding.toolbar.showCustomIcon(true)
        mBinding.toolbar.setOnRightClickListener {
//            mToolbarWindow?.showAsDropDown(it, Gravity.BOTTOM)
            showCustomMenu(it)
        }

        mBinding.toolbar.setOnbackListener {
            getMainActivity()?.drawerToggle()
        }
    }

    private fun initViewSetting() {
        mBinding.inclueDemoLayout1.let {
            it.tvDemoName.text = "Photo scanner"
            it.tvBtn.setOnClickListener {
                Launch.skipPhotographActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout2.let {
            it.tvDemoName.text = "Question"
            it.tvBtn.setBlockingOnClickListener {
                Launch.skipAnswerActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout3.let {
            it.tvDemoName.text = "Python-run"
            it.tvBtn.setOnClickListener {
                Launch.skipCodingActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout4.let {
            it.tvDemoName.text = "Ai-chat"
            it.tvBtn.setOnClickListener {
                Launch.skipAIIMActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout5.let {
            it.tvDemoName.text = "Datebase"
            it.tvBtn.setOnClickListener {
                Launch.skipChatActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout6.let {
            it.tvDemoName.text = "Vedio-player"
            it.tvBtn.setOnClickListener {
                Launch.skipPlayerManageActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout7.let {
            it.tvDemoName.text = "Question-2"
            it.tvBtn.setOnClickListener {
//                Log.d("Language", "当前语言: ${Locale.getDefault().language}")
//                Log.d("Language", "完整地区: ${Locale.getDefault()}")
//                activity?.recreate()
                Launch.skipQuizActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout8.let {
            it.tvDemoName.text = "Upload"
            it.tvBtn.setOnClickListener {
                Launch.skipUploadActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout9.let {
            it.tvDemoName.text = "Bus"
            it.tvBtn.setOnClickListener {
                setLogout()
                LiveDataBus.post(HomeEvent(HomeEvent.EVENT_LOGOUT))
                LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
//                finish()
            }
        }
        mBinding.inclueDemoLayout10.let {
            it.tvDemoName.text = "Floating Window"
            it.tvBtn.setOnClickListener {
                if (mRemovableWindow?.isShowing == true) {
                    cancelAndRecycleEasyWindow(mRemovableWindow)
                } else {
                    mRemovableWindow?.show()
                }
            }
        }
        mBinding.inclueDemoLayout11.let {
            it.tvDemoName.text = "Crash"
            it.tvBtn.setOnClickListener {
//                throw RuntimeException("Test Crash") // Force a crash
                val testNum = 100
//                val result = 100 / 0 // 这行代码会导致崩溃
                throw RuntimeException("${System.currentTimeMillis().toString()} 测试崩溃")
//                getBaseActivity()?.showAppUpgradeDialog(it)
            }
        }
        mBinding.inclueDemoLayout12.let {
            it.tvDemoName.text = "MathView"
            it.tvBtn.setOnClickListener {
                Launch.skipZoomImageActivity(getSupportContext())
            }
        }

        mBinding.inclueDemoLayout13.let {
            it.tvDemoName.text = "StepBar"
            it.tvBtn.setOnClickListener {
                Launch.skipStepBarViewActivity(getSupportContext())
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
        mRemovableWindow?.recycle()
        super.onDestroyView()
    }


}