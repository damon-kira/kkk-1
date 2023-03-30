package com.util.lib.uuid

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.core.content.PermissionChecker
import com.project.util.AESUtil
import com.util.lib.*
import com.cache.lib.SharedPrefGlobal
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import java.util.*

/**
 * Created by weishl on 2022/1/17
 *
 */
class UUIDHelper {

    companion object {
        private val DEBUG = BuildConfig.DEBUG
        private const val TAG = "debug_UUIDHelper"
        const val DEFAULT_DEVICES_ID = ".devicesId"
        const val DEFAULT_MEDIA_FILE_NAME = "980abc1"
        private const val PWD = "q1w2e3r4t5y"
    }

    private var mCtx: Context? = null

    private val mStorage: DevicesStorage by lazy(LazyThreadSafetyMode.NONE) {
        DevicesStorage()
    }

    constructor(context: Context) {
        mCtx = context
        mStorage.init(context)
    }

    fun init(context: Context) {
        mCtx = context
        mStorage.init(context)
    }

    @MainThread
    fun getMainUUid(): String {
        var uuid = getSpUUid()
        if (uuid.isEmpty()) {
            mCtx?.let {
                uuid = createCashUUID(it)
                save2Sp(uuid)
            }
        }
        return uuid
    }

    @WorkerThread
    fun getAesUUid(): String {
        val uuid = "${getUUid()}+${System.currentTimeMillis()}"
        logger_d(TAG, "getAesUUid: uuid = $uuid")
        return AESUtil.mexicoEncrypt(uuid, AesInfo.UUID_KEY, AesInfo.UUID_IV, false).orEmpty()
    }

    fun getMediaUUid(context: Context): String {
        return MediaStoreUtil.getUUID(context, DEFAULT_MEDIA_FILE_NAME)
    }

    fun saveMediaUUid(context: Context, uuid: String) {
        MediaStoreUtil.saveUUID(context, uuid, DEFAULT_MEDIA_FILE_NAME)
    }

    /**
     * 获取sp中的，sd中的比对时间戳
     */
    @WorkerThread
    fun getUUid(): String {
        val ctx = mCtx

        // 在缓存中取
        val localUUid = getLocalFileUUid()
        logger_e(TAG, "getUUid  localUUid = $localUUid")

        // 在sd卡中读取
        val storageUUid = getStorageUUid()
        logger_e(TAG, "getUUid  storageUUid = $storageUUid")
        // 内存卡中有，优先使用内存卡的
        if (checkPermission()) {
            if (storageUUid.trim().isNotEmpty()) {
                if (localUUid != storageUUid) {
                    saveUUid(storageUUid)
                }
                return storageUUid
            }
        }

        if (localUUid.trim().isNotEmpty()) {
            saveUUid(localUUid)
            return localUUid
        }

        // 以上都没有，创建UUID，并保存到以上文件中
        var uuid = ""
        ctx?.let {
            uuid = createUUID(ctx)
        }
        logger_d(TAG, "getUUid: create = $uuid")
        saveUUid(uuid)
        return uuid
    }

    private fun getLocalFileUUid(): String {
        // 在缓存中取
        var uuid = getSpUUid()

        if (uuid.isNullOrEmpty()) {
            mCtx?.let {
                uuid = UUidFile.readFile(it)
            }
        }
        return uuid
    }

    private fun getStorageUUid(): String {
        var storageUUid = mStorage.getUUid().orEmpty()
        storageUUid = storageUUid.decrypt()
        // 如果内存卡中是空的，则读取媒体文件中的
        if (storageUUid.isNullOrEmpty()) {
            // 读取媒体文件
            mCtx?.let {
                storageUUid = MediaStoreUtil.getUUID(it, DEFAULT_MEDIA_FILE_NAME)
            }
        }
        return storageUUid
    }

    fun saveUUid(uuid: String) {
        val encrypt = uuid.encrypt()
        save2Sp(encrypt)
        mCtx?.let {ctx ->
            UUidFile.writeCashDeviceIdFile(ctx, uuid, true)
        }
        // 权限允许后
        if (checkPermission()) {
            mStorage.saveUUid(encrypt)
            saveMedia(uuid) // 存媒体用的是原始UUID，文件用的是加密uuid
        }
    }

    private fun String.encrypt(): String{
        if (this.isNullOrEmpty()) return ""
        return FileUtils.DES_encrypt(this, PWD)
    }

    private fun String.decrypt(): String {
        if (this.isNullOrEmpty()) return ""
        return FileUtils.DES_decrypt(this, PWD)
    }

    private fun saveMedia(uuid: String) {
        if (checkPermission()) {
            mCtx?.let { ctx ->
                MediaStoreUtil.saveUUID(ctx, uuid, DEFAULT_MEDIA_FILE_NAME)
            }
        }
    }

    private fun save2Sp(uuid: String) {
        SharedPrefGlobal.setString(DEFAULT_DEVICES_ID, uuid)
    }

    fun getSpUUid(): String {
        val uuid = SharedPrefGlobal.getString(DEFAULT_DEVICES_ID, "")
        return uuid.decrypt()
    }

    /**
     * 获取UUID
     *
     * @return
     */
    private fun createUUID(context: Context): String {
        return createCashUUID(context)
    }

    private fun checkPermission():Boolean {
        val ctx = mCtx ?: return true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionChecker.checkSelfPermission(ctx, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED
                && PermissionChecker.checkSelfPermission(ctx, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
                return true
            }
        } else {
            return true
        }
        return false
    }


    /**
     * 获取UUID
     *
     * @return
     */
    private fun generateCashUUID(): String {
        var id: String = ""
        try {
            id = UUID.randomUUID().toString()
            id = id.replace("-".toRegex(), "").replace(":", "").toLowerCase()
        } catch (ex: Exception) {
            if (DEBUG) {
                Log.e(TAG, ex.toString())
            }
        }

        return id
    }

    private fun createCashUUID(context: Context): String {
        var deviceId: String? = null
        val builder = StringBuilder()
        try {
            deviceId = generateCashUUID()
            if (UUidCheck.invalidCashDeviceId(context, deviceId)) {
                deviceId = FileUtils.getRandomString(Random().nextInt(32) + 68)
            }

            if (DEBUG) {
                Log.d(TAG, "UUID UUID=${deviceId}")
            }
        } catch (ex: Exception) {
            deviceId = FileUtils.getRandomString(Random().nextInt(32) + 68)
        }
        builder.append("U_").append(deviceId)
        return builder.toString()
    }


//    fun delete(context: Context) {
//        save2Sp("")
//        mStorage.deleteUUid()
//        UUidFile.writeCashDeviceIdFile(context,"", false)
//    }

}