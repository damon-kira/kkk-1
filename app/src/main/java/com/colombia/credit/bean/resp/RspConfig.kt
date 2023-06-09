package com.colombia.credit.bean.resp

class RspConfig {
    val PjuFXVzXgcd9iiRBgT7T: String? = null // 是否显示语音电话
    val c3lzX3N3aXRjZV9ldmFsdWF0ZV9zY29yZQ:String? = null // 获取好评弹窗开关配置

    fun isShowVoice() = PjuFXVzXgcd9iiRBgT7T == "1"

    fun isOpen() = c3lzX3N3aXRjZV9ldmFsdWF0ZV9zY29yZQ.equals("open", true)
}