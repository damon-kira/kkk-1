package com.kira.learning.module.process.personalinfo

import com.kira.learning.bean.AddressInfo
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.module.process.BaseProcessViewModel
import com.kira.learning.utils.GPInfoUtils
import com.google.gson.reflect.TypeToken
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传基本信息
class PersonalViewModel @Inject constructor(private val repository: PersonalRepository) :
    BaseProcessViewModel() {

    val mAddrLiveData = generatorLiveData<ArrayList<AddressInfo>?>()

    private var mCache: ArrayList<AddressInfo>? = null

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            if (isUploadSuccess) {
                GPInfoUtils.saveTag(GPInfoUtils.TAG2)
            }
            mUploadLiveData.postValue(it)
        }
    }

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@addSourceLiveData
                if (data.ovabhwbahsSBHs == null || data.ovabhwbahsSBHs.isEmpty()) return@addSourceLiveData
                mInfoLiveData.postValue(data)
            }
        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
        if (isUploadSuccess) {
            removeCacheInfo()
            return
        }
        repository.saveCacheInfo(info)
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        return repository.getCacheInfo()
    }

    override fun removeCacheInfo() {
        repository.removeCacheInfo()
    }

    fun getAddrInfo() {
        if (mCache != null) {
            mAddrLiveData.postValue(mCache!!)
            return
        }
        mAddrLiveData.addSourceLiveData(repository.getAddrInfo()) {
            val info = GsonUtil.fromJson(it, object : TypeToken<ArrayList<AddressInfo>>() {})
            mAddrLiveData.postValue(info)
            mCache = info
        }
    }
}