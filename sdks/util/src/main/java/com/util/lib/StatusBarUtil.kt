package com.util.lib

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.max


/*
 *
 * Author: jinguang
 * Create: 2020/7/1 10:51
 * Description:
 */

object StatusBarUtil {
    private val FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view
    private val FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view
    /**
     * 官方处理方法
     */
    fun Activity.statusNormal() {
        val winParams = window.attributes
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        winParams.flags = bits
        window.attributes = winParams
    }


    /**
     * 设置根布局参数
     */
    private fun setRootView(activity: Activity) {
        val parent = activity.findViewById<View>(android.R.id.content) as ViewGroup
        var i = 0
        val count = parent.childCount
        while (i < count) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(true)
                childView.clipToPadding = true
            }
            i++
        }
    }

    /**
     * 目前只处理5.0以上的版本
     * 设置全屏 颜色需要透明
     * 非全屏的话 需要设置状态栏颜色
     * @param isDark 是否是黑色字体
     */
    fun Activity.setStatusBar(isFullScreen :Boolean = false, @ColorRes color: Int = R.color.transparent, isDark: Boolean = true) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        var vis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (isFullScreen) {
            vis = vis or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        // 6.0及以上
        if (isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = vis
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    /**
     * 目前只处理5.0以上的版本
     * 设置全屏 颜色需要透明
     * 非全屏的话 需要设置状态栏颜色
     * @param isDark 是否是黑色字体
     */
    fun Activity.setStatusBarColor(@ColorInt color: Int, isDark: Boolean = true) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        var vis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // 6.0及以上
        if (isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = vis
        window.statusBarColor = color
    }


    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */

    fun setColor(
        activity: Activity, @ColorInt color: Int, statusBarAlpha: Int = 0
    ) {
        //5.0以上版本直接设置状态栏颜色透明度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = color
            //4.4-5.0版本通过伪装一个状态栏来设置颜色和透明度
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val decorView = activity.window.decorView as ViewGroup
            val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.visibility == View.GONE) {
                    fakeStatusBarView.visibility = View.VISIBLE
                }
                fakeStatusBarView.setBackgroundColor(color)
            } else {
                decorView.addView(createStatusBarView(activity, color, statusBarAlpha))
            }
            setRootView(activity)
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }


    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private fun createStatusBarView(activity: Activity, @ColorInt color: Int, alpha: Int): View {
        // 绘制一个和状态栏一样高的矩形
        val statusBarView = View(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusBarView.layoutParams = params
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha))
        statusBarView.id = FAKE_STATUS_BAR_VIEW_ID
        return statusBarView
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    fun updateViewHeight(view: View) {
        view.layoutParams?.let {
            it.height = max((getStatusBarHeight(view.context) - 1f.dp()),0)
            view.layoutParams = it
        }
    }

}