package com.bigdata.lib

import android.Manifest
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import com.bigdata.lib.net.BigDataNetBaseParamsManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i

/**
 * Created by weishl on 2020/12/31
 *
 */
object ContactsMclcHelper {

    private const val TAG = "debug_ContactHelper"

    /**
     * 获取联系人信息
     */
    fun getContacts(): JsonArray {
        val manager = BigDataManager.get().getNetDataListener()
        if (manager != null) {
            if (BigDataNetBaseParamsManager.isPermissionAuth(Manifest.permission.READ_CONTACTS) == 0) {
                logger_i(TAG, " contacts = 没有权限")
                return JsonArray()
            }
        }
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            getContactsAbove8()
        } else getContactsUnder8()
    }


    /** 8.0及以下 */
    private fun getContactsUnder8(): JsonArray {
        val jsonArray = JsonArray()
        var cursor: Cursor? = null
        try {
            val manager = BigDataManager.get().getNetDataListener()
            if (manager == null) {
                logger_i(TAG, " bigdata not init")
                return JsonArray()
            }

            val resolver = manager.getContext().contentResolver
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI // 联系人Uri；
            // 按照sort_key升序查詢
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP,
                ContactsContract.CommonDataKinds.Phone.DATA6,
                ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED,
                ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED,
                ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
            )
            cursor = resolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                var jsonObject: JsonObject
                while (cursor.moveToNext()) {
                    jsonObject = JsonObject()
                    //联系人姓名
                    jsonObject.addProperty("name", cursor.getString(0).orEmpty())
                    //联系人电话
                    jsonObject.addProperty(
                        "tel",
                        BigDataNetBaseParamsManager.formatPhone(cursor.getString(1).orEmpty())
                    )
                    //最后更新时间
                    jsonObject.addProperty(
                        "last_update_time",
                        BigDataNetBaseParamsManager.formatPhone(cursor.getString(2).orEmpty())
                    )
                    jsonObject.addProperty("address", cursor.getString(3).orEmpty())
                    jsonObject.addProperty("times_contacted", cursor.getLong(4))
                    jsonObject.addProperty("last_time_contacted", cursor.getLong(5))
                    val typeAndData = cursor.getString(6).orEmpty()
                    val is_sim = if (typeAndData.contains("sim", true)) {
                        1
                    } else 0
                    jsonObject.addProperty("is_sim", is_sim)
                    val groupId = cursor.getString(7)
                    val groupName = getGroupName(groupId)
                    jsonObject.addProperty("group", groupName)
                    jsonArray.add(jsonObject)
                }
                if (isDebug()) {
                    logger_i(TAG, " contacts = $jsonArray")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, " getContacts e = $e")
        } finally {
            close(cursor)
        }
        return jsonArray
    }

    /** 8.0以上 */
    private fun getContactsAbove8(): JsonArray {
        val jsonArray = JsonArray()
        var cursor: Cursor? = null
        try {
            val manager = BigDataManager.get().getNetDataListener()
            if (manager == null) {
                logger_i(TAG, " bigdata not init")
                return JsonArray()
            }
            val resolver = manager.getContext().contentResolver
            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI // 联系人Uri；
            // 按照sort_key升序查詢
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP
            )
            cursor = resolver.query(uri, projection, null, null, null)
            if (cursor != null) {
                var jsonObject: JsonObject
                while (cursor.moveToNext()) {
                    jsonObject = JsonObject()
                    //联系人姓名
                    jsonObject.addProperty("name", cursor.getString(0).orEmpty())
                    //联系人电话
                    jsonObject.addProperty(
                        "tel",
                        BigDataNetBaseParamsManager.formatPhone(cursor.getString(1).orEmpty())
                    )
                    //最后更新时间
                    jsonObject.addProperty(
                        "last_update_time",
                        BigDataNetBaseParamsManager.formatPhone(cursor.getString(2).orEmpty())
                    )
                    jsonObject.addProperty("address", "")
                    jsonObject.addProperty("times_contacted", "")
                    jsonObject.addProperty("last_time_contacted", "")
                    jsonObject.addProperty("is_sim", "0")
                    jsonObject.addProperty("group", "")
                    jsonArray.add(jsonObject)
                }
                if (isDebug()) {
                    logger_i(TAG, " contacts = $jsonArray")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, " getContacts e = $e")
        } finally {
            close(cursor)
        }
        return jsonArray
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
        }finally {
            close(cursor)
        }
        return groupName
    }
}