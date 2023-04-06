package com.colombia.credit.util.image.worker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.permission.CameraPermission
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.reqPermission
import com.colombia.credit.permission.showNoPermissionDialog
import com.colombia.credit.util.FileUtils
import com.colombia.credit.util.image.ImagePathUtil
import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.annotations.CameraFace
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.ResultData
import com.colombia.credit.util.image.data.TakePhotoParams
import com.colombia.credit.util.image.exception.BaseException
import java.io.File

class TakePhotoWorker(container: AgentContainer, params: TakePhotoParams) :
    BaseWorker<TakePhotoParams, ResultData>(container, params) {

    override fun start(flowData: ResultData?, callback: ResultCallback<ResultData>) {
        val activity = container.getActivity()
        if (activity == null) {
            callback.onFailed(BaseException("activity is null"))
            return
        }
        val permission = Manifest.permission.CAMERA
        if (params.checkPermission) {
            if (PermissionHelper.checkPermission(activity, permission)) {
                takePhoto(activity, callback)
            } else {
                activity.reqPermission({ _: Boolean, granted: Boolean ->
                    if (granted) {
                        takePhoto(activity, callback)
                    } else {
                        callback.onFailed(Exception("no permission"))
                        //权限拒绝后弹窗提醒
                        activity.showNoPermissionDialog(
                            arrayListOf(CameraPermission()),
                            rightListener = {
                                activity.jumpToAppSettingPage()
                            })
                    }
                }, permission)
            }
        } else {
            takePhoto(activity, callback)
        }
    }


    private fun takePhoto(activity: Activity, callback: ResultCallback<ResultData>) {
        val intent = when (params.cameraFace) {
            CameraFace.FRONT -> {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(
                        "android.intent.extras.CAMERA_FACING",
                        Camera.CameraInfo.CAMERA_FACING_FRONT
                    )
                    putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
                    putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
                }


            }
            else -> {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            }
        }
        if (null === intent.resolveActivity(activity.packageManager)) {
            callback.onCancel()
            return
        }

        val saveFile = params.fileToSave ?: ImagePathUtil.createTempFile(container.getActivity()!!)
        val uri = FileUtils.createUriFromFile(activity, saveFile)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val modeFlags =
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.addFlags(modeFlags)
//            activity.grantUriPermission(activity.packageName, uri, modeFlags)
//            activity.revokeUriPermission(uri, modeFlags)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        try {
            container.startActivityResult(
                intent,
                FunctionManager.REQ_CAMERA_CODE
            ) { _: Int, resultCode: Int, _: Intent? ->
                handleResult(resultCode, saveFile, callback)
            }
        } catch (e: Exception) {
            callback.onFailed(e)
        }
    }

    private fun handleResult(
        resultCode: Int,
        saveFile: File,
        callback: ResultCallback<ResultData>
    ) {
        if (resultCode == Activity.RESULT_CANCELED) {
            callback.onCancel()
            return
        }
        val result = ResultData()
        result.uri = Uri.fromFile(File(saveFile.absolutePath))
        callback.onSuccess(result)
    }
}