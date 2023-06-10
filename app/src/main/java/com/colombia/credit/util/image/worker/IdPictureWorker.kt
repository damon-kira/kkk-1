package com.colombia.credit.util.image.worker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.camera.CaptureActivity
import com.colombia.credit.permission.CameraPermission
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.reqPermission
import com.colombia.credit.permission.showNoPermissionDialog
import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.IdPictureParams
import com.colombia.credit.util.image.data.ResultData
import com.common.lib.base.BaseActivity
import java.io.File

class IdPictureWorker(container: AgentContainer, params: IdPictureParams) :
    BaseWorker<IdPictureParams, ResultData>(container, params) {

    override fun start(flowData: ResultData?, callback: ResultCallback<ResultData>) {
        val activity = (container.getActivity() as? BaseActivity) ?: return
        val permission = Manifest.permission.CAMERA
        if (params.checkPermission) {
            if (PermissionHelper.checkPermission(activity, permission)) {
                capturePhoto(activity, callback)
            } else {
                activity.reqPermission({ _: Boolean, granted: Boolean ->
                    if (granted) {
                        capturePhoto(activity, callback)
                    } else {
                        callback.onFailed(Exception("no permission"))
                        //权限拒绝后弹窗提醒
                        activity.showNoPermissionDialog(
                            arrayListOf(CameraPermission()),
                            rightListener = {
                                //没有权限跳转至设置
                                activity.jumpToAppSettingPage()
                            })
                    }
                }, permission)
            }
        } else {
            capturePhoto(activity, callback)
        }
    }

    private fun capturePhoto(activity: Activity, callback: ResultCallback<ResultData>) {
        val capturePath = params.fileToSave
        val intent = Intent(activity, CaptureActivity::class.java).apply {
            putExtra(CaptureActivity.KEY_CAPTURE_IMAGE_PATH, capturePath)
            putExtra(CaptureActivity.KEY_PICTURE_TYPE, params.picType)
        }
        try {
            container.startActivityResult(
                intent,
                FunctionManager.REQ_CUSTOM_PHOTO_CODE
            ) { _: Int, resultCode: Int, _: Intent? ->
                handleResult(resultCode, callback)
            }
        } catch (e: Exception) {
            callback.onFailed(e)
        }
    }

    private fun handleResult(resultCode: Int, callback: ResultCallback<ResultData>) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val result = ResultData()
                result.uri = Uri.fromFile(File(params.fileToSave))
                callback.onSuccess(result)
            }
            Activity.RESULT_CANCELED -> {
                callback.onCancel()
            }
            else -> {
                callback.onFailed(Exception("failed"))
            }
        }
    }
}