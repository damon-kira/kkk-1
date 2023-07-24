package com.bigdata.lib

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import androidx.core.content.PermissionChecker
import com.bigdata.lib.bean.ContactInfo
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.io.IOException

// 联系人获取
object ContactsHelper {

    private const val TAG = "debug_ContactHelper"

    /**
     * 获取联系人信息
     */
    fun getContacts(context: Context): ArrayList<ContactInfo> {
        if (PermissionHelper.isPermissionAuth(
                context,
                Manifest.permission.READ_CONTACTS
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            logger_i(TAG, " contacts = 没有权限")
            return arrayListOf()
        }
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            getContactsAbove8(context)
        } else getContactsUnder8(context)
    }


    /** 8.0及以下 */
    private fun getContactsUnder8(context: Context): ArrayList<ContactInfo> {
        val list = arrayListOf<ContactInfo>()
        var cursor: Cursor? = null
        try {
            val resolver = context.contentResolver
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI // 联系人Uri；
            // 按照sort_key升序查詢

            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
//                ,
//                ContactsContract.CommonDataKinds.Phone.DATA6,
//                ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED,
//                ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED,
//                ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET,
//                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
            )
            val selection =
                ContactsContract.CommonDataKinds.StructuredName.IN_VISIBLE_GROUP + " = 1"
            val sortOrder =
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
            cursor = resolver.query(uri, projection, selection, null, sortOrder)
            if (cursor != null) {
                val firstNameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
                val lastNameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)

                var info: ContactInfo
                while (cursor.moveToNext()) {
                    info = ContactInfo()
                    //联系人姓名
                    info.daQgH = cursor.getString(0).orEmpty()
//                    if (firstNameIndex != -1) {
//                        info.cr8c7xX = cursor.getString(firstNameIndex)
//                    }
                    if (lastNameIndex != -1) {
                        info.UpokGx = cursor.getString(lastNameIndex)
                    }
                    info.Uy3I = formatPhone(cursor.getString(1).orEmpty())//联系人电话
                    info.hpEIahj2fG = formatPhone(cursor.getString(2).orEmpty()) //最后更新时间
//                    info.address = cursor.getString(3).orEmpty()
//                    info.timesContacted = cursor.getLong(4)
//                    info.lastTimeContacted = cursor.getLong(5)
//                    val typeAndData = cursor.getString(6).orEmpty()
//                    val is_sim = if (typeAndData.contains("sim", true)) {
//                        1
//                    } else 0
//                    info.is_sim = is_sim
//                    val groupId = cursor.getString(7)
//                    val groupName = getGroupName(groupId)
//                    info.group = groupName
                    list.add(info)
                }
                if (isDebug()) {
                    logger_i(TAG, " contacts = $list")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, " getContacts e = $e")
        } finally {
            close(cursor)
        }
        return list
    }

    /** 8.0以上 */
    private fun getContactsAbove8(context: Context): ArrayList<ContactInfo> {
        val list = arrayListOf<ContactInfo>()
        var cursor: Cursor? = null
        try {
            val resolver = context.contentResolver
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI // 联系人Uri；
            // 按照sort_key升序查詢
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
            )
            val selection =
                ContactsContract.CommonDataKinds.StructuredName.IN_VISIBLE_GROUP + " = 1"
            val sortOrder =
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
            cursor = resolver.query(uri, projection, selection, null, sortOrder)
            if (cursor != null) {

                var info: ContactInfo
                while (cursor.moveToNext()) {
                    info = ContactInfo()
                    //联系人姓名
                    info.daQgH = cursor.getString(0).orEmpty()
                    val firstNameIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
                    val lastNameIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
//                    if (firstNameIndex != -1) {
//                        info.cr8c7xX = cursor.getString(firstNameIndex)
//                    }
                    if (lastNameIndex != -1) {
                        info.UpokGx = cursor.getString(lastNameIndex)
                    }
                    info.Uy3I = formatPhone(cursor.getString(1).orEmpty())//联系人电话
                    info.hpEIahj2fG = formatPhone(cursor.getString(2).orEmpty()) //最后更新时间

//                    info.address = ""
//                    info.group = ""
                    list.add(info)
                }
                if (isDebug()) {
                    logger_i(TAG, " contacts = $list")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, " getContacts e = $e")
        } finally {
            close(cursor)
        }
        return list
    }


    private fun getGroupName(groupId: String): String {
        val uri = ContactsContract.Groups.CONTENT_URI
        val projection = arrayOf(ContactsContract.Groups.TITLE)
        val selection = "${ContactsContract.Groups._ID}=?"
        var groupName = ""
        var cursor: Cursor? = null
        val manager = BigDataManager.get().getNetDataListener()
        if (manager == null) {
            logger_i(TAG, " bigdata not init")
            return ""
        }
        try {
            cursor =
                manager.getContext().contentResolver.query(
                    uri,
                    projection,
                    selection,
                    arrayOf(groupId),
                    null
                )
                    ?: return ""
            if (cursor.moveToNext()) {
                groupName = cursor.getString(0).orEmpty()
            }
        } catch (e: Exception) {
        } finally {
            close(cursor)
        }
        return groupName
    }

    /**
     * 格式化手机号
     */
    private fun formatPhone(phoneNumber: String): String {
        return phoneNumber.replace(" ", "")
    }

    private fun close(cursor: Cursor?): Boolean {
        try {
            cursor?.close()
            return true
        } catch (e: IOException) {
        }
        return false
    }
}