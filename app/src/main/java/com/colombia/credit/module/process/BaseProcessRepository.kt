package com.colombia.credit.module.process

import androidx.lifecycle.LiveData
import com.cache.lib.SharedPrefUser
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.req.IReqBaseInfo
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil

abstract class BaseProcessRepository<T : IReqBaseInfo> : BaseRepository(), IBaseProcessRepository {

    override fun saveCacheInfo(info: IReqBaseInfo) {
        SharedPrefUser.setString(getCacheKey(), GsonUtil.toJson(info))
    }

    override fun removeCacheInfo() {
        SharedPrefUser.removeKey(getCacheKey())
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        val cache = SharedPrefUser.getString(getCacheKey(), null)
        return GsonUtil.fromJson(cache, getCacheClass()) as IReqBaseInfo
    }

    abstract fun getCacheClass(): Class<T>

    abstract fun getCacheKey(): String
}

interface IBaseProcessRepository {

    fun uploadInfo(info: IReqBaseInfo): LiveData<BaseResponse<String>>

    fun saveCacheInfo(info: IReqBaseInfo)

    fun getCacheInfo(): IReqBaseInfo?

    fun removeCacheInfo()
}