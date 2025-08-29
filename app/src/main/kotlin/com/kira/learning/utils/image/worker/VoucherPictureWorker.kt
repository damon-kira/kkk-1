package com.kira.learning.utils.image.worker

import android.Manifest
import android.app.Activity
import android.content.Intent
import com.kira.learning.manager.Launch.jumpToAppSettingPage
import com.kira.learning.permission.CameraPermission
import com.kira.learning.permission.PermissionHelper
import com.kira.learning.permission.reqPermission
import com.kira.learning.permission.showNoPermissionDialog
import com.kira.learning.utils.image.agent.AgentContainer
import com.kira.learning.utils.image.callback.ResultCallback
import com.kira.learning.utils.image.data.ResultData
import com.kira.learning.utils.image.data.VoucherPictureParams
import com.common.lib.base.BaseActivity


class VoucherPictureWorker(container: AgentContainer, params: VoucherPictureParams) : BaseWorker<VoucherPictureParams, ResultData>(container, params) {

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
                        activity.showNoPermissionDialog(arrayListOf(CameraPermission()), rightListener = {
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
        /*val capturePath = params.fileToSave
        val intent = Intent(activity, VoucherPictureActivity::class.kotlin)
        intent.putExtra(BaseCaptureActivity.FILE_PATH, capturePath)
        try {
            container.startActivityResult(intent, FunctionManager.REQ_VOUCHER_PICTURE_CODE) { _: Int, resultCode: Int, dataIntent: Intent? ->
                handleResult(resultCode, dataIntent, callback)
            }
        } catch (e: Exception) {
            callback.onFailed(e)
        }*/
    }

    private fun handleResult(resultCode: Int, dataIntent: Intent?, callback: ResultCallback<ResultData>) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val result = ResultData()
                result.uri = dataIntent!!.data
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