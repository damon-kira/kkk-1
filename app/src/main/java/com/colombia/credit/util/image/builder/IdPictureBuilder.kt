package com.colombia.credit.util.image.builder

import com.camera.lib.CameraOneUtils.Companion.CAMERA_BACK
import com.colombia.credit.util.image.ImagePathUtil
import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.annotations.CapturePhotoType
import com.colombia.credit.util.image.data.IdPictureParams
import com.colombia.credit.util.image.data.ResultData
import com.colombia.credit.util.image.worker.IdPictureWorker
import com.colombia.credit.util.image.worker.Worker
import java.io.File

class IdPictureBuilder(functionManager: FunctionManager) : BaseBuilder<ResultData>(functionManager) {

    private var fileToSave: File? = null

    private var type = CAMERA_BACK

    private var checkPermission = true


    fun fileToSave(fileToSave: File): IdPictureBuilder {
        this.fileToSave = fileToSave
        return this
    }

    fun type(@CapturePhotoType captureType: Int): IdPictureBuilder {
        this.type = captureType
        return this
    }

    fun checkPermission(check: Boolean = true): IdPictureBuilder {
        this.checkPermission = check
        return this
    }


    override fun createWorker(): Worker<ResultData> {
        val path = fileToSave
                ?: ImagePathUtil.createTempFile(functionManager.container.getActivity()!!)
        val params = IdPictureParams(type, path.absolutePath, checkPermission)
        return IdPictureWorker(functionManager.container, params)
    }
}
