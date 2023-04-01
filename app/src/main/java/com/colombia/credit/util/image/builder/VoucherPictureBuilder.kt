package com.colombia.credit.util.image.builder

import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.data.ResultData
import com.colombia.credit.util.image.data.VoucherPictureParams
import com.colombia.credit.util.image.worker.VoucherPictureWorker
import com.colombia.credit.util.image.worker.Worker
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
