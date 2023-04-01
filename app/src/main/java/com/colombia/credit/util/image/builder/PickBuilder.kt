package com.colombia.credit.util.image.builder

import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.annotations.ImageType
import com.colombia.credit.util.image.annotations.ImageType.Companion.ALL
import com.colombia.credit.util.image.annotations.PickRange
import com.colombia.credit.util.image.annotations.PickRange.Companion.PICK_DICM
import com.colombia.credit.util.image.data.PickPictureParams
import com.colombia.credit.util.image.data.ResultData
import com.colombia.credit.util.image.worker.PickWorker
import com.colombia.credit.util.image.worker.Worker

class PickBuilder(functionManager: FunctionManager) : BaseBuilder<ResultData>(functionManager) {

    private var pickRange = PICK_DICM

    private var fileType = ALL

    private var checkPermission = true


    private var needLocationInfo = false


    fun range(@PickRange pickRange: Int = PICK_DICM): PickBuilder {
        this.pickRange = pickRange
        return this
    }

    fun type(@ImageType type: Int = ALL): PickBuilder {
        this.fileType = type
        return this
    }

    fun checkPermission(check: Boolean = true): PickBuilder {
        this.checkPermission = check
        return this
    }

    fun needLocationInfo(need: Boolean = false): PickBuilder {
        this.needLocationInfo = need
        return this
    }

    override fun createWorker(): Worker<ResultData> {
        val params = PickPictureParams(pickRange, fileType, checkPermission,needLocationInfo)
        return PickWorker(functionManager.container, params)
    }

}