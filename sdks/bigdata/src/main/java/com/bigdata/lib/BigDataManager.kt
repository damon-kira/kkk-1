package com.bigdata.lib

import com.bigdata.lib.net.NetConfigDataInterface


/**
 *@author zhujun
 *@description:
 *@date : 2022/2/28 4:26 下午
 */
class BigDataManager private constructor(){
    companion object {

        fun get(): BigDataManager {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = BigDataManager()
    }

    private var dataListener: NetConfigDataInterface? = null

    fun getNetDataListener(): NetConfigDataInterface?{
        return dataListener
    }

    fun setNetDataListener(listener: NetConfigDataInterface){
        dataListener = listener
    }

}