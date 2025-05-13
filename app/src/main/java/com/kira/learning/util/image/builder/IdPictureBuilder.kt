package com.kira.learning.util.image.builder

import com.camera.lib.CameraOneUtils.Companion.CAMERA_BACK
import com.kira.learning.util.image.ImagePathUtil
import com.kira.learning.util.image.FunctionManager
import com.kira.learning.util.image.annotations.CapturePhotoType
import com.kira.learning.util.image.annotations.PicType
import com.kira.learning.util.image.data.IdPictureParams
import com.kira.learning.util.image.data.ResultData
import com.kira.learning.util.image.worker.IdPictureWorker
import com.kira.learning.util.image.worker.Worker
import java.io.File

class IdPictureBuilder(functionManager: FunctionManager) : BaseBuilder<ResultData>(functionManager) {

    private var fileToSave: File? = null

    private var type = CAMERA_BACK

    private var picType = PicType.PIC_FRONT

    private var checkPermission = true


    fun fileToSave(fileToSave: File): IdPictureBuilder {
        this.fileToSave = fileToSave
        return this
    }

    fun type(@CapturePhotoType captureType: Int): IdPictureBuilder {
        this.type = captureType
        return this
    }

    fun picType(@PicType picType: Int): IdPictureBuilder {
        this.picType = picType
        return this
    }

    fun checkPermission(check: Boolean = true): IdPictureBuilder {
        this.checkPermission = check
        return this
    }




    override fun createWorker(): Worker<ResultData> {
        val path = fileToSave
                ?: ImagePathUtil.createTempFile(functionManager.container.getActivity()!!)
        val params = IdPictureParams(type, picType, path.absolutePath, checkPermission)
        return IdPictureWorker(functionManager.container, params)
    }
}
