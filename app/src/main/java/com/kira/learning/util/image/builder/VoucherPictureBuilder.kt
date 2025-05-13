package com.kira.learning.util.image.builder

import com.kira.learning.util.image.FunctionManager
import com.kira.learning.util.image.data.ResultData
import com.kira.learning.util.image.data.VoucherPictureParams
import com.kira.learning.util.image.worker.VoucherPictureWorker
import com.kira.learning.util.image.worker.Worker
import java.io.File

class VoucherPictureBuilder(functionManager: FunctionManager) : BaseBuilder<ResultData>(functionManager) {

    private var fileToSave: File? = null

    private var checkPermission = true

    fun fileToSave(fileToSave: File): VoucherPictureBuilder {
        this.fileToSave = fileToSave
        return this
    }

    fun checkPermission(check: Boolean = true): VoucherPictureBuilder {
        this.checkPermission = check
        return this
    }

    override fun createWorker(): Worker<ResultData> {
        val params = VoucherPictureParams(fileToSave?.absolutePath ?: "", checkPermission)
        return VoucherPictureWorker(functionManager.container, params)
    }


}
