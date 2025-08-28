package com.kira.learning.module.process

import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.res.IRspBaseInfo
import com.kira.learning.bean.res.RspResult
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse

abstract class BaseProcessViewModel : BaseViewModel(), IBaseProcessViewModel {

    protected var isUploadSuccess: Boolean = false // 是否上传成功，上传成功不保存进件填写信息
        set(value) {
            if (value) {
                removeCacheInfo()
            }
            field = value
        }

    val mUploadLiveData = generatorLiveData<BaseResponse<RspResult>>()

    val mInfoLiveData = generatorLiveData<IRspBaseInfo>()
}

interface IBaseProcessViewModel {

    fun uploadInfo(info: IReqBaseInfo)

    fun getInfo()

    fun saveCacheInfo(info: IReqBaseInfo)

    fun getCacheInfo(): IReqBaseInfo?

    fun removeCacheInfo()
}