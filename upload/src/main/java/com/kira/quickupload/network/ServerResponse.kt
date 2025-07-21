package com.kira.quickupload.network

import android.os.Parcelable
import java.io.Serializable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServerResponse(
    /**
     * 服务器响应响应代码。如果您正在实现非HTTP
     * 协议，将此设置为200以通知任务已完成
     * 成功。小于200或大于299表示的整数值
     * 来自服务器的错误响应。
     */
    val code: Int,

    /**
     * 服务器响应正文。
     * 如果你的服务器响应一个字符串，你可以得到它
     * 如果字符串是JSON，则可以使用org.json等库对其进行解析
     * 如果你的服务器没有返回任何东西，设置为空数组。
     */
    val body: ByteArray,
    /**
     * 服务器响应标头
     */
    val headers: LinkedHashMap<String, String>
) : Parcelable, Serializable {

    /**
     * 获取服务器响应正文作为字符串。
     * 如果字符串是JSON，则可以使用org.json等库对其进行解析
     * @ 返回字符串
     */
    @IgnoredOnParcel
    val bodyString: String
        get() = String(body)

    @IgnoredOnParcel
    val isSuccessful: Boolean
        get() = code in 200..399

    companion object {
        fun successfulEmpty(): ServerResponse {
            return ServerResponse(
                code = 200, body = ByteArray(1), headers = LinkedHashMap()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerResponse

        if (code != other.code) return false
        if (!body.contentEquals(other.body)) return false
        if (headers != other.headers) return false
        if (isSuccessful != other.isSuccessful) return false
        if (bodyString != other.bodyString) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + body.contentHashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + isSuccessful.hashCode()
        result = 31 * result + bodyString.hashCode()
        return result
    }
}
