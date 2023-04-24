package com.colombia.credit.module.process

import androidx.lifecycle.LiveData
import com.cache.lib.SharedPrefUser
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.resp.IRspBaseInfo
import com.colombia.credit.bean.resp.RspResult
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import com.util.lib.log.logger_d

abstract class BaseProcessRepository<Rsp : IRspBaseInfo, T : IReqBaseInfo> : BaseRepository(),
    IBaseProcessRepository<Rsp> {

    override fun saveCacheInfo(info: IReqBaseInfo) {
        SharedPrefUser.setString(getCacheKey(), GsonUtil.toJson(info))
    }

    override fun removeCacheInfo() {
        SharedPrefUser.removeKey(getCacheKey())
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        val cache = SharedPrefUser.getString(getCacheKey(), null)
        if (cache.isEmpty()) return null
        logger_d("debug_BaseProcessRepository", "getCacheInfo: cache = $cache  class =${getCacheClass()}")
        return GsonUtil.fromJsonNew(cache, getCacheClass()) as? IReqBaseInfo
    }

    abstract fun getCacheClass(): Class<T>

    abstract fun getCacheKey(): String
}

interface IBaseProcessRepository<Rsp> {

    fun uploadInfo(info: IReqBaseInfo): LiveData<BaseResponse<RspResult>>

    fun getInfo(): LiveData<BaseResponse<Rsp>>

    fun saveCacheInfo(info: IReqBaseInfo)

    fun getCacheInfo(): IReqBaseInfo?

    fun removeCacheInfo()
}