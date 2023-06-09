package com.colombia.credit.module.config

import com.colombia.credit.bean.resp.RspConfig
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import javax.inject.Inject


// 配置信息
class ConfigViewModel @Inject constructor(private val repository: ConfigRepository): BaseViewModel() {

    companion object {
        const val KEY_VOICE = "PjuFXVzXgcd9iiRBgT7T" // 语音配置
        const val KEY_GP_PJ = "c3lzX3N3aXRjZV9ldmFsdWF0ZV9zY29yZQ" // gp评价配置
    }

    var configLiveData = generatorLiveData<RspConfig>()

    var mConfig: RspConfig? = null

    fun getConfig(keys: String){
        showloading()
        repository.getConfig(keys).observerNonStickyForever {
            hideLoading()
            if (it.isSuccess()) {
                mConfig = it.getData()
                mConfig?.let { config ->
                    configLiveData.postValue(config)
                }
            } else {
                configLiveData.postValue(RspConfig())
            }
        }
    }
}