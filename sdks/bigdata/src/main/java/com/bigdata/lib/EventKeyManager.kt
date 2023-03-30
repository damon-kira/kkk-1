package com.bigdata.lib

object EventKeyManager {

    const val EVENT_ACTIVATION = 10000//激活

    interface ConstantDot {
        companion object {
            //打点 大数据上报结果
            const val PAGE_BIG_DATA = "9999"
            const val EVENT_RESULT_UPLOAD = "$PAGE_BIG_DATA-0"
            const val EVENT_RESULT_OK = "$PAGE_BIG_DATA-1"
            const val EVENT_RESULT_FAILED = "$PAGE_BIG_DATA-2"
        }
    }
}