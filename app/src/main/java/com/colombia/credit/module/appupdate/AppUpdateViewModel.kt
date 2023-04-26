package com.colombia.credit.module.appupdate

import com.colombia.credit.bean.resp.AppUpgradeInfo
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import javax.inject.Inject

// 获取App更新信息
class AppUpdateViewModel @Inject constructor(private val repository: AppUpdateRepository) :
    BaseViewModel() {

    val updateLiveData = generatorLiveData<AppUpgradeInfo>()

    fun getAppUpdate() {
        repository.getAppUpdate().observerNonStickyForever {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@observerNonStickyForever
                if (data.bt35AvNbu == 0 || data.bt35AvNbu == 3) {
                    return@observerNonStickyForever
                }
                updateLiveData.postValue(data)
            }
        }
    }
}