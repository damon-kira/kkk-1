package com.colombia.credit.module.process

import androidx.lifecycle.LiveData
import com.cache.lib.SharedPrefUser
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.IBaseInfo
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil

abstract class BaseProcessRepository<T : IBaseInfo> : BaseRepository(), IBaseProcessRepository {

    override fun saveCacheInfo(info: IBaseInfo) {
        SharedPrefUser.setString(getCacheKey(), GsonUtil.toJson(info))
    }

    override fun removeCacheInfo() {
        SharedPrefUser.removeKey(getCacheKey())
    }

    override fun getCacheInfo(): IBaseInfo? {
        val cache = SharedPrefUser.getString(getCacheKey(), null)
        return GsonUtil.fromJson(cache, getCacheClass()) as IBaseInfo
    }

    abstract fun getCacheClass(): Class<T>

    abstract fun getCacheKey(): String
}

interface IBaseProcessRepository {

    fun uploadInfo(info: IBaseInfo): LiveData<BaseResponse<String>>

    fun saveCacheInfo(info: IBaseInfo)

    fun getCacheInfo(): IBaseInfo?

    fun removeCacheInfo()
}