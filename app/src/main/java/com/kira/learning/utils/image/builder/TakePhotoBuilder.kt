package com.kira.learning.utils.image.builder

import com.kira.learning.utils.image.FunctionManager
import com.kira.learning.utils.image.annotations.CameraFace
import com.kira.learning.utils.image.callback.ResultCallback
import com.kira.learning.utils.image.data.ResultData
import com.kira.learning.utils.image.data.TakePhotoParams
import com.kira.learning.utils.image.worker.TakePhotoWorker
import java.io.File

class TakePhotoBuilder(functionManager: FunctionManager) : BaseBuilder<ResultData>(functionManager) {

    private var cameraFace = CameraFace.BACK

    private var fileToSave: File? = null

    private var callback: ResultCallback<ResultData>? = null

    private var checkPermission = true

    fun callback(callback: ResultCallback<ResultData>): TakePhotoBuilder {
        this.callback = callback
        return this
    }

    fun fileToSave(fileToSave: File): TakePhotoBuilder {
        this.fileToSave = fileToSave
        return this
    }

    fun checkPermission(check: Boolean = true): TakePhotoBuilder {
        this.checkPermission = check
        return this
    }


    fun cameraFace(@CameraFace face: Int = CameraFace.BACK): TakePhotoBuilder {
        this.cameraFace = face
        return this
    }

    override fun createWorker(): TakePhotoWorker {
        val params = TakePhotoParams(cameraFace,fileToSave,checkPermission)
        return TakePhotoWorker(functionManager.container,params)
    }



}
