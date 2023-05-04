package com.colombia.credit.util.image.worker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.util.lib.ImageInfoUtil
import com.colombia.credit.util.image.ImagePathUtil
import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.builder.ImageCompressor
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.CompressParams
import com.colombia.credit.util.image.data.CompressResult
import com.colombia.credit.util.image.data.ResultData
import com.colombia.credit.util.image.exception.BaseException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*


class CompressWorker(container: AgentContainer, params: CompressParams) :
    BaseWorker<CompressParams, CompressResult>(container, params), ImageCompressor {
    override fun start(flowData: ResultData?, callback: ResultCallback<CompressResult>) {

        val sourceUri = flowData?.uri ?: params.source
        if (sourceUri == null) {
            callback.onFailed(Exception("sourcePath error"))
            return
        }
        startCompress(sourceUri, callback)
    }

    private fun startCompress(
        source: Uri,
        callback: ResultCallback<CompressResult>
    ) {
        val activity = container.getActivity()
        if (activity == null) {
            callback.onFailed(BaseException("activity is null"))
            return
        }
        val outputFile = params.fileToSave ?: ImagePathUtil.createInternalTempFile(activity)
        val dis = Observable.just(params)
            .map {
                val compressor = it.customCompressor ?: this@CompressWorker
                val compressResult = compressor.compress(
                    source,
                    outputFile,
                    it.bitmapConfig,
                    it.compressFormat,
                    it.quality,
                    it.targetWidth,
                    it.targetHeight
                )
                compressResult
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it) {
                    val compressData = CompressResult()
                    compressData.sourceUri = source
                    compressData.uri = Uri.fromFile(outputFile)
                    callback.onSuccess(compressData)
                } else {
                    callback.onFailed(BaseException("compress failed"))
                }
            }, {
                callback.onFailed(it)
            })
    }


    override fun compress(
        source: Uri,
        outputFile: File,
        bitmapConfig: Bitmap.Config,
        compressFormat: Bitmap.CompressFormat,
        quality: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Boolean {
        val activity = container.getActivity()!!
        val fd: FileDescriptor =
            activity.contentResolver.openFileDescriptor(source, "r")?.fileDescriptor!!
        var bitmap = BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFileDescriptor(fd, null, this)
            inSampleSize = calculateInSampleSize(this, params.targetWidth, params.targetHeight)
            inJustDecodeBounds = false
            inPreferredConfig = bitmapConfig
            inTempStorage = ByteArray(16 * 1024)
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
                inPurgeable = true
                inInputShareable = true
            }
            BitmapFactory.decodeFileDescriptor(fd, null, this)
        }

        val ins: InputStream = activity.contentResolver.openInputStream(source)!!
        val angle: Int = getRotateDegree(ExifInterface(ins))
        bitmap = rotateImage(bitmap, angle)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(outputFile)
            bitmap.compress(compressFormat, quality, fos)
        } finally {
            fos?.close()
        }
        return true
    }


    /**
     * 获取图片的旋转角度
     * 只能通过原始文件获取，如果已经进行过 bitmap 操作无法获取。
     */
    private fun getRotateDegree(exif: ExifInterface): Int {
        var result = 0
        try {
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> result = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> result = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> result = 270
            }
        } catch (ignore: IOException) {
            return 0
        }
        return result
    }

    private fun rotateImage(bitmap: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     *
     * 根据图片原始尺寸和需求尺寸计算压缩比例 -> https://developer.android.google.cn/reference/android/graphics/BitmapFactory.Options#inSampleSize
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        if (targetWidth <= 0 || targetHeight <= 0) return 1
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > targetHeight || width > targetWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= targetHeight && halfWidth / inSampleSize >= targetWidth) inSampleSize *= 2
        }
        return inSampleSize
    }


}