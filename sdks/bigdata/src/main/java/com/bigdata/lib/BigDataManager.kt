package com.bigdata.lib

import com.bigdata.lib.net.NetConfigDataInterface


class BigDataManager private constructor() {
    companion object {

        fun get(): BigDataManager {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = BigDataManager()
    }

    private var dataListener: NetConfigDataInterface? = null

    fun getNetDataListener(): NetConfigDataInterface? {
        return dataListener
    }

    fun setNetDataListener(listener: NetConfigDataInterface) {
        dataListener = listener
    }

    var uploadListener: ((result: Boolean) -> Unit)? = null

}