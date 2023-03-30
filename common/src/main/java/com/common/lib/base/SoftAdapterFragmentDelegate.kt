package com.common.lib.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.util.lib.MainHandler
import com.util.lib.dp
import com.util.lib.log.logger_d

/**
 * author: weishl
 * data: 2019/11/30
 *
 * fragment 软键盘适配
 **/
class SoftAdapterFragmentDelegate constructor(private val softAdapter: ISoftAdapter,private val context: Context) {

    private val TAG = "debug_SoftAdapterFragmentDelegate"

    private var mFragment: Fragment? = null

    private var lastSoftHeight = 0
    private var lastScrollDistance = 0

    private var animator: ValueAnimator? = null

    // 实际显示的高度
    private var mRealDisplayHeight: Int = 0

    init {
        mFragment = softAdapter as? Fragment
    }

    fun onCreate(savedInstanceState: Bundle?) {
        val scrollView = getScrollView() ?: return
        getRealDisplayHeight()
        scrollView.postDelayed({
            if (!softAdapter.isDestroyViewSoft()) {
                scrollView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
                if (isActive()){
                    onLayoutChange()
                }
            }
        }, 350L)
    }

    fun onDestroy() {
        getScrollView()?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        mFragment = null
        animator?.cancel()
    }

    fun onFragmentVisibilityChanage(visible: Boolean) {
        if (!visible && lastScrollDistance != 0) {
            scrollToPos(lastScrollDistance, 0)
        }
    }

    private fun getRealDisplayHeight() {
        mFragment?.activity?.let {
            val rect = Rect()
            it.window.decorView.getWindowVisibleDisplayFrame(rect)
            mRealDisplayHeight = rect.height()
        }
    }

    private fun getScrollView(): ViewGroup? {
        return softAdapter.getScrollView()
    }

    private fun getTargetY(): Int {
        return softAdapter.getTargetY()
    }


    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        onLayoutChange()
    }

    private fun onLayoutChange() {
        val activity = mFragment?.activity
        val rect = Rect()
        //getWindowVisibleDisplayFrame 获取当前窗口可视区域大小
        activity?.apply {
            window.decorView.getWindowVisibleDisplayFrame(rect)

            val softHeight = mRealDisplayHeight - rect.bottom

            var scrollDistance =
                getTargetY() + (softAdapter.getMarginBottom()).dp() - rect.height()

            logger_d(TAG, "onGlobalLayout>>softHeight=$softHeight,scrollDistance=$scrollDistance")
//            if (lastScrollDistance == 0 && scrollDistance < 0) return@OnGlobalLayoutListener
            MainHandler.post {
                getScrollView()?.run {
                    if (scrollDistance < 0) {
                        scrollDistance = 0
                    }
                    if (softHeight > 0) {
                        //具体移动距离可自行调整
                        if (lastSoftHeight == 0) {
                            scrollToPos(0, scrollDistance)
                        } else if (lastScrollDistance != scrollDistance && scrollDistance != 0) {//键盘高度有细微变化
                            scrollToPos(lastScrollDistance, scrollDistance, 100L)
                        }
                    } else if (lastScrollDistance != 0) {
                        //键盘隐藏，页面复位
                        scrollToPos(lastScrollDistance, 0)
                    }
                    lastSoftHeight = softHeight
                }
            }
        }
    }

    private fun scrollToPos(start: Int, end: Int, durationTime: Long = 300L) {
        if (animator?.isRunning == true) {
            animator?.cancel()
        }
        lastScrollDistance = end
        animator = ValueAnimator.ofInt(start, end).apply {
            duration = durationTime
            addUpdateListener { valueAnimator ->
                getScrollView()?.scrollTo(
                    0,
                    valueAnimator.animatedValue as Int
                )
            }
            start()
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    @SuppressLint("ServiceCast")
    private fun isActive(): Boolean {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isActive
    }
}