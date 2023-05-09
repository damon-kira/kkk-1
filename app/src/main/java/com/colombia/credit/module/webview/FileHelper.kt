package com.colombia.credit.module.webview

import android.net.Uri
import android.os.SystemClock
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.colombia.credit.util.image.ImageObtainHelper
import com.colombia.credit.util.image.annotations.ImageType
import com.colombia.credit.util.image.annotations.PickRange
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.CompressResult
import com.common.lib.base.BaseFragment
import com.util.lib.expand.getCacheFile
import java.io.File


class FileHelper(private var fragment: BaseFragment) {

    private var mAcceptType: String? = null
    private var mValueCallback: ValueCallback<Array<Uri>>? = null

    fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams?
    ): Boolean {
        if (fileChooserParams?.acceptTypes.isNullOrEmpty().not()) {
            mAcceptType = fileChooserParams?.acceptTypes?.get(0)
        }
        mValueCallback = filePathCallback
        if (fileChooserParams?.isCaptureEnabled == true) {
            takePhoto()
        } else {
            takePicture()
        }
        return true
    }

    private fun takePhoto() {
        val targetPath =
            getCacheFile(fragment.getSupportContext()).absolutePath + "/" + SystemClock.currentThreadTimeMillis() + ".jpg"
        ImageObtainHelper
            .createAgent(fragment)
            .take()
            .checkPermission(true)
            .then()
            .compress()
            .fileToSave(File(targetPath))
            .start(object : ResultCallback<CompressResult> {
                override fun onSuccess(result: CompressResult) {
                    val filePath = result.uri?.path ?: return
                    callback(arrayOf(Uri.fromFile(File(filePath))))
                }

                override fun onCancel() {
                    callback(null)
                }

                override fun onFailed(e: Throwable) {
                    callback(null)
                }
            })
    }

    private fun takePicture() {
        val targetPath =
            getCacheFile(fragment.getSupportContext()).absolutePath + "/" + SystemClock.currentThreadTimeMillis() + ".jpg"
        ImageObtainHelper
            .createAgent(fragment)
            .pick()
            .range(PickRange.PICK_DICM)
            .type(ImageType.ALL)
            .then()
            .compress()
            .fileToSave(File(targetPath))
            .start(object : ResultCallback<CompressResult> {
                override fun onSuccess(result: CompressResult) {
                    val filePath = result.uri?.path ?: return
                    callback(arrayOf(Uri.fromFile(File(filePath))))
                }

                override fun onCancel() {
                    callback(null)
                }

                override fun onFailed(e: Throwable) {
                    callback(null)
                }
            })
    }

    private fun callback(params: Array<Uri>?) {
        mValueCallback?.onReceiveValue(params)
        mValueCallback = null
    }
}