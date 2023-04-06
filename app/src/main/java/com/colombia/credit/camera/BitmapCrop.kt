package com.colombia.credit.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.media.ExifInterface
import com.common.lib.base.BaseActivity
import com.util.lib.DisplayUtils
import com.util.lib.image.ExifInterfaceImpl
import com.util.lib.image.calculateInSampleSize
import com.util.lib.image.commonCompressPic
import com.util.lib.image.getPhotoOrientation
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

object BitmapCrop {

    private const val TAG = "debug_BitmapCrop"

    fun crop(
        activity: BaseActivity,
        originFile: File,
        rect: Rect,
        isFront: Boolean,
        result: (File?) -> Unit
    ) {
        val dispose = crop(activity, originFile, rect, isFront).subscribe({
            logger_e(TAG, "success = ${it.length()}")
            result.invoke(it)
        }, {
            logger_e(TAG, "error = $it")
            result.invoke(null)
        })
    }


    fun cropAndCompress(
        activity: BaseActivity,
        originFile: File,
        rect: Rect,
        isFront: Boolean,
        result: (File?) -> Unit
    ) {
        val dispose =
            crop(activity, originFile, rect, isFront).doOnNext {
                commonCompressPic(
                    it.absolutePath,
                    it.absolutePath,
                    360,
                    640
                )
            }.subscribe({
                logger_e(TAG, "success = ${it.length()}")
                result.invoke(it)

            }, {
                logger_e(TAG, "error = $it")
                result.invoke(null)
            })
    }

    /**
     * @param isFront true:前置  false:后置
     */
    private fun crop(
        activity: BaseActivity,
        originFile: File,
        rect: Rect,
        isFront: Boolean
    ): Flowable<File> {
        return Flowable.fromPublisher {
            try {
                val screenW = DisplayUtils.getRealScreenWidth(activity)
                val screenH = DisplayUtils.getRealScreenHeight(activity)

                val ops = BitmapFactory.Options()
                ops.inJustDecodeBounds = true
                BitmapFactory.decodeFile(originFile.absolutePath, ops)
                val exif = ExifInterfaceImpl(originFile.absolutePath)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
                )
                val rotation = getPhotoOrientation(orientation).toFloat()
                val matrix = Matrix()
                matrix.postRotate(rotation)
                ops.inSampleSize = if (ops.outHeight < ops.outWidth) {
                    calculateInSampleSize(
                        ops,
                        screenH,
                        screenW
                    )
                } else {
                    calculateInSampleSize(
                        ops,
                        screenW,
                        screenH
                    )
                }
                ops.inJustDecodeBounds = false
                var bitmap = BitmapFactory.decodeFile(originFile.absolutePath, ops)

                val bitmapWidth = bitmap.width.toFloat()
                val bitmapHeight = bitmap.height.toFloat()
                logger_i(TAG, "屏幕宽高：screenW:$screenW screenH:$screenH rotation: $rotation")
                var left = 0
                var right = 0
                var top = rect.top
                var bottom = rect.bottom - rect.top

                logger_i(TAG, "bitmap width = $bitmapWidth  height=$bitmapHeight")

                if (isFront)
                    matrix.postScale(-1f, 1f)

                val scale = if (rotation / 90 % 2 == 0f) {
                    val widthScale = screenW / bitmapWidth
                    val heightScale = screenH / bitmapHeight
                    if (isScaleCanUsed(widthScale, bitmap, rect, isFront)) {
                        widthScale
                    } else {
                        heightScale
                    }
                } else {
                    val widthScale = screenW / bitmapHeight
                    val heightScale = screenH / bitmapWidth
                    if (isScaleCanUsed(widthScale, bitmap, rect, isFront)) {
                        widthScale
                    } else {
                        heightScale
                    }
                }
                if (bitmapWidth < rect.right || bitmapHeight < rect.bottom) {
                    matrix.postScale(scale, scale)
                }

                bitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                // 前置
                if (isFront) {
                    left = if (bitmap.width - rect.width() > 0) {
                        (bitmap.width - rect.width()) / 2
                    } else rect.left
                    right = rect.right - rect.left
                } else {
                    // 后置
                    left = rect.left
                    top = rect.top
                    right = rect.right - rect.left
                    bottom = rect.bottom - rect.top
                }

                if (right + left > bitmap.width) {
                    right = bitmap.width
                    left = max(bitmap.width - rect.width(), 0)
                }

                bitmap = Bitmap.createBitmap(bitmap, left, top, right, bottom)
                originFile.delete()
                val fos = FileOutputStream(originFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
                fos.flush()
                fos.close()
                it.onNext(originFile)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun isScaleCanUsed(
        scale: Float,
        bitmap: Bitmap,
        scannerRect: Rect,
        isFront: Boolean
    ): Boolean {
        var resultBitmap: Bitmap = bitmap
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        resultBitmap = Bitmap.createBitmap(
            resultBitmap,
            0,
            0,
            resultBitmap.width,
            resultBitmap.height,
            matrix,
            true
        )
        var left = 0
        var right = 0
        val top = scannerRect.top
        val bottom = scannerRect.bottom - top

        if (isFront) {// 前置
            left = if (resultBitmap.width - scannerRect.width() > 0) {
                (resultBitmap.width - scannerRect.width()) / 2
            } else scannerRect.left
            right = scannerRect.right - scannerRect.left
        } else {// 后置
            left = scannerRect.left
            right = scannerRect.right - scannerRect.left
        }
        if ((right + left) > resultBitmap.width) {
            right = resultBitmap.width
        }

        if (resultBitmap.height < bottom) {
            logger_d(
                TAG,
                "当前 scale:$scale 不符合  resultBitmap.height: ${resultBitmap.height} < bottom${bottom}"
            )
            return false
        }
        if (resultBitmap.width < right) {
            logger_d(
                TAG,
                "当前 scale:$scale 不符合  resultBitmap.height: ${resultBitmap.width} < right：${right}"
            )
            return false
        }
        return true
    }

}