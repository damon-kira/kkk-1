package com.kira.learning.utils.camera

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.kira.learning.BuildConfig
import java.lang.reflect.Field

class UtilUI {
    private var barHeight = -1

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return 状态栏高度
     */
    fun getBarHeight(context: Context): Int {
        if (barHeight == -1) {
            var c: Class<*>? = null
            var obj: Any? = null
            var field: Field? = null
            var x = 0

            try {
                c = Class.forName("com.android.internal.R\$dimen")
                obj = c.newInstance()
                field = c.getField("status_bar_height")
                x = field.get(obj).toString().toInt()
                barHeight = context.resources.getDimensionPixelSize(x)
            } catch (e1: Exception) {
                e1.printStackTrace()
                return 0
            }
        }
        return barHeight
    }

    /**
     * 获取屏幕截屏 【不包含状态栏】
     *
     * @param activity
     * @param containTopBar 是否包含状态栏
     * @return
     */
    fun getScreenshot(activity: Activity, containTopBar: Boolean): Bitmap? {
        try {
            val window = activity.window
            val view = window.decorView
            view.setDrawingCacheEnabled(true)
            view.buildDrawingCache(true)
            val bmp1 = view.drawingCache

            /**
             * 除去状态栏和标题栏
             */
            val height = if (containTopBar) 0 else getBarHeight(activity)
            return Bitmap.createBitmap(bmp1, 0, height, bmp1.getWidth(), bmp1.getHeight() - height)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 获取Activity截图
     *
     * @param activity
     * @return bitmap 截图
     */
    fun getDrawing(activity: Activity): Bitmap? {
        val view = (activity.findViewById<View?>(R.id.content) as ViewGroup).getChildAt(0)
        return getDrawing(view)
    }

    /**
     * 获取View截图
     *
     * @param view
     * @return 截图
     */
    fun getDrawing(view: View): Bitmap? {
        try {
            view.setDrawingCacheEnabled(true)
            var tBitmap = view.getDrawingCache()
            // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
            tBitmap = Bitmap.createBitmap(tBitmap)
            view.setDrawingCacheEnabled(false)
            return tBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        /**
         * 检查webview是否可用
         *
         * @return
         */
        fun isWebviewValid(context: Context): Boolean {
            requireNotNull(context) { "context can't be null !!" }
            try {
                val webView = WebView(context)
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    throw e
                }
                //            CrashManager.reportException(e);
                return false
            }
            return true
        }
    }
}
