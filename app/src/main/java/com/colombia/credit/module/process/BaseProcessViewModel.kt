package com.colombia.credit.module.process

import com.colombia.credit.bean.resp.IBaseInfo
import com.common.lib.base.BaseViewModel
import com.common.lib.net.bean.BaseResponse

abstract class BaseProcessViewModel : BaseViewModel(), IBaseProcessViewModel {

    val mUploadLiveData = generatorLiveData<BaseResponse<String>>()
}

interface IBaseProcessViewModel {

    fun uploadInfo(info: IBaseInfo)

    fun saveCacheInfo(info: IBaseInfo)

    fun getCacheInfo(): IBaseInfo?

    fun removeCacheInfo()
}