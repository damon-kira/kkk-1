package com.kira.learning.camera

import android.app.Activity
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import com.util.lib.log.logger_e

class BlurUtil {


    private var mActivity: Activity? = null

    /**
     * 工具箱
     */
    private var utils: UtilBox? = null

    /**
     * 构造
     * @param activity
     */
    constructor(activity: Activity) {
        this.mActivity = activity
        utils = UtilBox.instance
    }

    /**
     * 模糊图片
     *
     * @param iv 需要模糊的ImageView
     */
    fun clickBlurImg(iv: ImageView) {
        // 将图片进行高斯模糊，
        // 最后一个参数是模糊等级，值为 0~25
        utils?.bitmap?.blurImageView(mActivity, iv, 10f)
    }

    fun clickBlurImg(iv: ImageView, captureView: View) {
        try {
            val drawingBitmap = if (captureView is TextureView) {
                var bitmap = captureView.bitmap
                if (captureView.rotation % 360 != 0f && null != bitmap) {
                    bitmap = utils?.bitmap?.rotate(bitmap, captureView.rotation.toInt())
                }
                bitmap
            } else {
                utils?.ui?.getDrawing(captureView)
            }
            if (drawingBitmap != null) {
                val blurBitmap = utils?.bitmap?.blurBitmap(mActivity, drawingBitmap, 20f)
                iv.setImageBitmap(blurBitmap)
            }
        } catch (e: Exception) {
            logger_e("debug_BlurUtil", "clickBlurImg error = $e")
        }
    }
}