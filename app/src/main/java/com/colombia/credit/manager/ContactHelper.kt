package com.colombia.credit.manager

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import com.colombia.credit.bean.PhoneAndName
import com.colombia.credit.permission.PermissionHelper


object ContactHelper {

    private val NAME_LENGTH = 30 //姓名最大长度

    /**
     * 根据URI查询联系人信息
     * @param isFormat true:返回手机号不包含 +52  name：最长不能超过30位
     */
    @JvmStatic
    fun queryContactInfo(context: Context, uri: Uri?, isFormat: Boolean = false): PhoneAndName {
        var contact = PhoneAndName("", "")
        uri ?: return contact
        contact = queryContact(context, uri)
        if (isFormat) {
            contact.name = parsingName(contact.name)
            contact.phone = parsingMobile(contact.phone)
        }
        return contact
    }

    /**
     * 根据电话号码查询联系人信息
     * @param isFormat true:返回手机号不包含 +52  name：最长不能超过30位
     */
    @JvmStatic
    fun queryContactInfo(context: Context, mobile: String, isFormat: Boolean = false): PhoneAndName {
        val uri = Uri.parse("content://com.android.contacts/data/phones/filter/$mobile")
        var contact = queryContact(context, uri)
        if (isFormat) {
            contact.name = parsingName(contact.name)
            contact.phone = parsingMobile(contact.phone)
        }
        return contact
    }

    private fun queryContact(context: Context, uri: Uri): PhoneAndName {
        var num: String? = null
        var name: String? = null
        val contact = PhoneAndName("", "")
        var cursor: Cursor? = null
        try {
            // 创建内容解析者
            val contentResolver = context.contentResolver
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP
            )
            cursor = contentResolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    name = cursor.getString(0)
                    num = cursor.getString(1).replace("-".toRegex(), "").replace(" ", "")
//                    num = cursor.getString(1).replace("[^0-9]".toRegex(), "")
//                    if (name != null) {
//                        name = name.replace("[^a-zA-Z0-9\\s\\u4e00-\\u9fa5]".toRegex(), "")
//                    }
                    break
                    //最后更新时间
//                val time =
//                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP))
//                        .orEmpty()
                }
            }
        } catch (e: Exception) {
        } finally {
            cursor?.close()
        }
        contact.phone = num.orEmpty()
        contact.name = name.orEmpty()
        return contact
    }


    private fun parsingName(name: String): String {
        if (name.length < NAME_LENGTH) {
            return name
        }
        return name.substring(0, NAME_LENGTH)
    }

    private fun parsingMobile(mobile: String): String {
//        if (mobile.length < 3) {
//            return mobile
//        }
//        return if (mobile.startsWith("+52")) {
//            mobile.substring(3)
//        } else if (mobile.startsWith("+")) {
//            mobile.substring(1)
//        } else if (mobile.startsWith("52")) {
//            mobile.substring(2)
//        } else {
//            mobile
//        }
        return mobile
    }

    /** Android 9.0是否有读取联系人权限 */
    fun checkPermission28(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                !PermissionHelper.checkPermission(context, Manifest.permission.READ_CONTACTS)
    }
}