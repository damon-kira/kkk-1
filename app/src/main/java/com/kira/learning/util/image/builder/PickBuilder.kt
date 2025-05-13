package com.kira.learning.util.image.builder

import com.kira.learning.util.image.FunctionManager
import com.kira.learning.util.image.annotations.ImageType
import com.kira.learning.util.image.annotations.ImageType.Companion.ALL
import com.kira.learning.util.image.annotations.PickRange
import com.kira.learning.util.image.annotations.PickRange.Companion.PICK_DICM
import com.kira.learning.util.image.data.PickPictureParams
import com.kira.learning.util.image.data.ResultData
import com.kira.learning.util.image.worker.PickWorker
import com.kira.learning.util.image.worker.Worker

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