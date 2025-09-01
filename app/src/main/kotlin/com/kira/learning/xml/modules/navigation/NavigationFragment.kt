package com.kira.learning.xml.modules.navigation

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
import com.kira.learning.xml.expand.setLogout
import com.kira.learning.manager.Launch
import com.kira.learning.xml.modules.home.BaseHomeFragment
import com.kira.learning.xml.modules.home.HomeEvent
import com.kira.learning.xml.modules.home.XMLMainActivity
import com.kira.learning.xml.modules.home.MainEvent
import com.kira.learning.xml.modules.home.vm.HomeLoanViewModel
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

        mHomeViewModel.mRspInfoLiveData.observe(viewLifecycleOwner) {

            val data = it.gQ1J
            if (data == null || data.isEmpty()) return@observe

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
//                    startActivity(Intent(this, SettingsActivity::class.kotlin))
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

    private fun getMainActivity() = activity as XMLMainActivity?

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
        mBinding.inclueDemoLayout0.let {
            it.tvDemoName.text = "Compose_main"
            it.tvBtn.setOnClickListener {
                Launch.skipComposeMainActivity(getSupportContext())
            }
        }
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
            it.tvDemoName.text = "Activities"
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
        mBinding.inclueDemoLayout14.let {
            it.tvDemoName.text = "WebView"
            it.tvBtn.setOnClickListener {
                Launch.skipWebViewActivity(
                    getSupportContext(), "https://onecompiler.com/embed?language=python"
                )
            }
        }
        mBinding.inclueDemoLayout15.let {
            it.tvDemoName.text = "Coding"
            it.tvBtn.setOnClickListener {
                Launch.skipCodePlaygroundActivity(getSupportContext())
//                Launch.skipKCodingEditorActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout16.let {
            it.tvDemoName.text = "SuperEditor"
            it.tvBtn.setOnClickListener {
                Launch.skipSuperEditorActivity(getSupportContext())
            }
        }
        mBinding.inclueDemoLayout17.let {
            it.tvDemoName.text = "Ai-Chat"
            it.tvBtn.setOnClickListener {
                Launch.skipAiChatActivity(getSupportContext())
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