package com.kira.learning.util.image.worker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import com.kira.learning.util.image.ImagePathUtil
import com.kira.learning.util.image.agent.AgentContainer
import com.kira.learning.util.image.builder.ImageCompressor
import com.kira.learning.util.image.callback.ResultCallback
import com.kira.learning.util.image.data.CompressParams
import com.kira.learning.util.image.data.CompressResult
import com.kira.learning.util.image.data.ResultData
import com.kira.learning.util.image.exception.BaseException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
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

        Observable.just(params)
            .flatMap { params ->
                Observable.fromCallable {
                    val compressor = params.customCompressor ?: this@CompressWorker
                    compressor.compress(
                        source,
                        outputFile,
                        params.bitmapConfig,
                        params.compressFormat,
                        params.quality,
                        params.targetWidth,
                        params.targetHeight
                    )
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                if (success) {
                    val compressData = CompressResult().apply {
                        sourceUri = source
                        uri = Uri.fromFile(outputFile)
                    }
                    callback.onSuccess(compressData)
                } else {
                    callback.onFailed(BaseException("compress failed"))
                }
            }, { error ->
                callback.onFailed(error)
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
        return try {
            // First pass - get image dimensions
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                activity.contentResolver.openFileDescriptor(source, "r")?.use { fd ->
                    BitmapFactory.decodeFileDescriptor(fd.fileDescriptor, null, this)
                }
                inSampleSize = calculateInSampleSize(this, targetWidth, targetHeight)
            }

            // Second pass - load compressed bitmap
            val bitmap = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                inPreferredConfig = bitmapConfig
                inTempStorage = ByteArray(16 * 1024)
                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
                    inPurgeable = true
                    inInputShareable = true
                }
                inSampleSize = options.inSampleSize
            }.let {
                activity.contentResolver.openFileDescriptor(source, "r")?.use { fd ->
                    BitmapFactory.decodeFileDescriptor(fd.fileDescriptor, null, it)
                }
            } ?: return false

            // Handle rotation
            activity.contentResolver.openInputStream(source)?.use { inputStream ->
                val rotatedBitmap = rotateImage(bitmap, getRotateDegree(ExifInterface(inputStream)))
                FileOutputStream(outputFile).use { fos ->
                    rotatedBitmap.compress(compressFormat, quality, fos)
                }
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun getRotateDegree(exif: ExifInterface): Int {
        return try {
            when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            0
        }
    }

    private fun rotateImage(bitmap: Bitmap, angle: Int): Bitmap {
        if (angle == 0) return bitmap
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            Matrix().apply { postRotate(angle.toFloat()) },
            true
        )
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        if (targetWidth <= 0 || targetHeight <= 0) return 1
        val (height, width) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > targetHeight || width > targetWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= targetHeight && halfWidth / inSampleSize >= targetWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}