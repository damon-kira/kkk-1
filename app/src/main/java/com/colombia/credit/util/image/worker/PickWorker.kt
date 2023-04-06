package com.colombia.credit.util.image.worker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import com.colombia.credit.manager.Launch.jumpToAppSettingPage
import com.colombia.credit.permission.PermissionHelper
import com.colombia.credit.permission.StoragePermission
import com.colombia.credit.permission.reqPermission
import com.colombia.credit.permission.showNoPermissionDialog
import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.annotations.ImageType
import com.colombia.credit.util.image.annotations.PickRange
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.PickPictureParams
import com.colombia.credit.util.image.data.ResultData

class PickWorker(container: AgentContainer, params: PickPictureParams) : BaseWorker<PickPictureParams, ResultData>(container, params) {

    override fun start(flowData: ResultData?, callback: ResultCallback<ResultData>) {
        val activity = container.getActivity() ?: return
        if (params.checkPermission) {
            val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE
//            val permissions = mutableListOf<String>()
//            permissions.add(storagePermission)
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && params.needLocationInfo){
//                permissions.add( Manifest.permission.ACCESS_MEDIA_LOCATION)
//            }
            if (PermissionHelper.checkPermission(activity, storagePermission)) {
                pickPhoto(activity, callback)
            } else {
                activity.reqPermission({ _: Boolean, granted: Boolean ->
                    if (granted) {
                        pickPhoto(activity, callback)
                    } else {
                        callback.onFailed(Exception("no permission"))
                        //权限拒绝后弹窗提醒
                        activity.showNoPermissionDialog(arrayListOf(StoragePermission()), rightListener = {
                            activity.jumpToAppSettingPage()
                        })
                    }
                }, storagePermission)
            }
        } else {
            pickPhoto(activity, callback)
        }

    }

    private fun pickPhoto(activity: Activity, callback: ResultCallback<ResultData>) {
        val pickIntent = if (params.pickRange == PickRange.PICK_DICM) {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        } else {
            Intent(Intent.ACTION_GET_CONTENT)
        }

        pickIntent.also {
            when (params.fileType) {
                ImageType.ALL -> {
                    it.type = "image/*"
                }
                ImageType.GIF -> {
                    it.type = "image/gif"
                }
                ImageType.PNG -> {
                    it.type = "image/png"
                }
                ImageType.JPEG -> {
                    it.type = "image/jpeg"
                }
            }
        }
        if (null == pickIntent.resolveActivity(activity.packageManager)) {
            callback.onFailed(Exception("activity status error"))
            return
        }
        try {
            container.startActivityResult(pickIntent, FunctionManager.REQ_PICK_CODE) { _: Int, resultCode: Int, data: Intent? ->
                handleResult(resultCode, data, callback)
            }
        } catch (e: Exception) {
            callback.onFailed(e)
        }
    }

    private fun handleResult(resultCode: Int, data: Intent?, callback: ResultCallback<ResultData>) {
        if (resultCode == Activity.RESULT_CANCELED) {
            callback.onCancel()
            return
        }
        if (null != data && null != data.data) {
            val result = ResultData()
            try {
//                result.dataPath = ImagePathUtil.uriToImagePath(container.getActivity()!!, data.data!!)
                result.uri = data.data!!
                callback.onSuccess(result)
            } catch (e: Exception) {
                callback.onFailed(e)
            }
        } else {
            callback.onFailed(Exception("null result intentData"))
        }
    }
}