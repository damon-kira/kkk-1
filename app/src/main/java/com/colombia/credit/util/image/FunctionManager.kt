package com.colombia.credit.util.image

import com.colombia.credit.util.image.builder.*
import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.worker.Worker

class FunctionManager(val container: AgentContainer) {

    companion object {
        const val REQ_PICK_CODE = 6543
        const val REQ_CAMERA_CODE = 6542
        const val REQ_CUSTOM_PHOTO_CODE = 6541
        const val REQ_VOUCHER_PICTURE_CODE = 6540
    }

    internal val workerFlows = ArrayList<Worker<*>>()

    /**
     * 相册选择
     */
    fun pick(): PickBuilder {
        return PickBuilder(this)
    }

    /**
     * 拍照
     */
    fun take(): TakePhotoBuilder {
        return TakePhotoBuilder(this)
    }

    /**
     * 自定义拍照样式
     */
    fun idPicture(): IdPictureBuilder {
        return IdPictureBuilder(this)
    }

    /**
     * 还款凭证拍照
     */
    fun voucherPicture(): VoucherPictureBuilder {
        return VoucherPictureBuilder(this)
    }


    /**
     * 图片压缩
     */
    fun compress(): CompressImageBuilder {
        return CompressImageBuilder(this)
    }


}