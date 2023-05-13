package com.colombia.credit.util

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.widget.EditText
import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.permission.SmsPermission
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.util.*
import java.util.regex.Pattern

/**
 * 自动获取验证码
 */
fun EditText.registerSmsObserver(
    observer: SmsContentObserver,
    autoFillCallback: (code: String) -> Unit = {}
) {
    if (!SmsPermission().hasThisPermission(getAppContext())) {
        return
    }
    logger_i("EditText", "注册绑定自动获取验证码")

    try {
        context.contentResolver.registerContentObserver(Uri.parse("content://sms/"), true, observer)
    } catch (e: SecurityException) {
        logger_e("debug_SmsUtil", "exception = $e")
        return
    }
    observer.setOnCodeCallBack(object : SmsContentObserver.OnCodeCallBack {
        override fun onCodeCallBackListener(code: String) {
            if (code.isEmpty())
                return
            setText(code)
            setSelection(code.length)
            autoFillCallback.invoke(code)
        }
    })
}

/**
 * 解绑
 */
fun EditText.unRegisterSmsObserver(observer: SmsContentObserver) {
    logger_i("EditText", "解除绑定自动获取验证码")
    try {
        context.contentResolver.unregisterContentObserver(observer)
    } catch (e: SecurityException) {
        logger_e("debug_SmsUtil", "exception = $e")
    }
}

class SmsContentObserver(private var mContext: Context, handler: Handler? = Handler()) :
    ContentObserver(handler) {
    companion object {
        private const val CONTENT_KEY = "Finanzas extremas"
        private const val INBOX_URI_PATH = "content://sms/inbox"
        private const val CODE_REGEX_PATTERN = """\d{4}"""
    }

    private var mContentKey = CONTENT_KEY
    private var mCodeRegexPattern = CODE_REGEX_PATTERN
    private var codeCallBack: OnCodeCallBack? = null

    /**
     * 点击“获取验证码”按钮的时间，此时间以后的才会被读取
     */
    private var receiverTime = Calendar.getInstance().timeInMillis

    fun updateReceiverTime() {
        receiverTime = Calendar.getInstance().timeInMillis
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        if (uri?.toString() == "content://sms/draft") {
            return
        }
        val inboxUri = Uri.parse(INBOX_URI_PATH)
        try {
            // 按时间顺序排序短信数据库
            mContext.contentResolver.query(
                inboxUri,
                null,
                null,
                null,
                "date desc"
            ) ?.use {c ->
                if (c.moveToFirst()) {
                    val dataIndex = c.getColumnIndex("date")
                    if (receiverTime >= c.getLong(dataIndex)) return
                    val bodyIndex = c.getColumnIndex("body")
                    val body = c.getString(bodyIndex)
                    logger_i("EditText", "body>>>$body")
                    val contains = body?.contains(mContentKey)
                    if (contains != null && !contains) {
                        return
                    }
                    val pattern = Pattern.compile(mCodeRegexPattern)
                    val matcher = pattern.matcher(body)
                    if (matcher.find()) {
                        val code = matcher.group(0)
                        codeCallBack?.onCodeCallBackListener(code)
                    }
                }
            }
        } catch (e: Exception) {
            logger_e("MessageContentObserver", e.toString())
        }
    }

    fun setOnCodeCallBack(codeCallBack: OnCodeCallBack?) {
        this.codeCallBack = codeCallBack
    }

    interface OnCodeCallBack {
        fun onCodeCallBackListener(code: String)
    }

    /**
     * 设置验证码正则表达式
     */
    fun setCodeRegexPattern(pattern: String) {
        mCodeRegexPattern = pattern
    }

    /**
     * 设置短信包含Key的设置
     */
    fun setMessageContentContainKey(contentKey: String) {
        mContentKey = contentKey
    }
}
