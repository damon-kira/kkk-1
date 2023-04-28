package com.colombia.credit.module.upload

import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.permission.ContactPermission
import com.colombia.credit.permission.SmsPermission
import com.common.lib.livedata.observerNonStickyForever
import javax.inject.Inject

class UploadViewModel @Inject constructor(private val repository: UploadRepository) :
    BaseProcessViewModel() {

    companion object {
        const val TYPE_SMS = 0x10
        const val TYPE_APP = 0x11
        const val TYPE_ALL = 0x12
        const val TYPE_CON = 0x13
    }

    val resultLiveData = generatorLiveData<RspResult>()

    val checkLiveData = generatorLiveData<Boolean>()

    override fun uploadInfo(info: IReqBaseInfo) {
        resultLiveData.addSourceLiveData(repository.uploadInfo()) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            resultLiveData.postValue(RspResult())
        }
    }

    override fun getInfo() {
        showloading()
        checkLiveData.addSourceLiveData(repository.getInfo()) {
            hideLoading()
            var result = false
            if (it.isSuccess()) {
                result = it.getData()?.isNew() == true
            }
            checkLiveData.postValue(result)
        }
    }

    fun checkAndUpload() {
        showloading()
        resultLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess() && it.getData()?.isNew() == true) {
                hideLoading()
                resultLiveData.postValue(RspResult())
            } else {
                uploadInfo(ReqKycInfo())
            }
        }
    }

    fun upload(type: Int) {
        when (type) {
            TYPE_SMS -> {
                if (SmsPermission().hasThisPermission(getAppContext()))
                    repository.uploadsms().observerNonStickyForever { }
            }
            TYPE_ALL -> {
                uploadInfo(ReqKycInfo())
            }
            TYPE_APP -> {
                repository.uploadApp().observerNonStickyForever { }
            }
            TYPE_CON -> {
                if (ContactPermission().hasThisPermission(getAppContext()))
                    repository.uploadCo().observerNonStickyForever { }
            }
        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {}
    override fun getCacheInfo(): IReqBaseInfo? = null
    override fun removeCacheInfo() {}
}