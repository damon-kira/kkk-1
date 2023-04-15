package com.colombia.credit.util

import androidx.exifinterface.media.ExifInterface
import com.cache.lib.SharedPrefUser
import com.google.gson.JsonObject
import com.util.lib.GsonUtil
import com.util.lib.log.logger_d
import java.io.File

object ImageInfoUtil {

    private const val TAG = "ImageInfoUtil"

    fun saveInfo(key: String, json: String) {
        SharedPrefUser.setString(key, json)
    }

    fun getInfo(key: String): String {
        return SharedPrefUser.getString(key, null)
    }

    //{ “extractGps”:””, 拍摄时GPS
    // ”extractTakeTime”:””, 拍摄日期
    // ”extractPohotoSize”:””,图片内存 100kb
    // ”extractPohotoPixel”:””,图片像素100x100”
    // extractTakeDevice”:””拍摄设备 }

    fun getExifInfo(path: String?): String? {
        path ?: return null
        val ex = ExifInterface(path)
        val exifInfo = getExifCommonInfo(ex)
        val dateTime = exifInfo[ExifInterface.TAG_DATETIME]
        val width = exifInfo[ExifInterface.TAG_IMAGE_WIDTH]
        val height = exifInfo[ExifInterface.TAG_IMAGE_LENGTH]
        val mode = exifInfo[ExifInterface.TAG_MAKE]
        val longitude = exifInfo[ExifInterface.TAG_GPS_LONGITUDE]
        val latitude = exifInfo[ExifInterface.TAG_GPS_LATITUDE]
        val jobj = JsonObject()
        jobj.addProperty("extractGps", "$longitude,$latitude")
        jobj.addProperty("extractTakeTime", dateTime)
        jobj.addProperty("extractPohotoSize", "${File(path).length() / 1000}kb")
        jobj.addProperty("extractPohotoPixel", "${width}x${height}")
        jobj.addProperty("extractTakeDevice", mode)
        return jobj.toString()
    }

    fun getImageExifInfo(path: String?): HashMap<String, String?> {
        val data = HashMap<String, String?>(50)
        path ?: return data
        try {
            val ex = ExifInterface(path);
            val exifInfo = getExifCommonInfo(ex)
            data.putAll(exifInfo)
            val field = ExifInterface::class.java.getDeclaredField("mAttributes")
            field.isAccessible = true
            when (val value: Any? = field.get(ex)) {
                is Array<*> -> {
                    value.filterIsInstance<HashMap<*, *>>().forEach {
                        data.putAll(getImageInfo(it, ex))
                    }
                }
                is HashMap<*, *> -> {
                    data.putAll(getImageInfo(value, ex))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        logger_d("debug_ImageInfoUtil", "getImageExifInfo: ${GsonUtil.toJson(data)}")
        return data
    }

    fun saveExifInfo(path: String, info: Map<String, String?>) {
        try {
            val ex = ExifInterface(path);
            info.entries.forEach {
                val value = it.value
                if (value != null || it.key == ExifInterface.TAG_IMAGE_LENGTH || it.key == ExifInterface.TAG_IMAGE_WIDTH)
                    ex.setAttribute(it.key, value)
            }
            ex.saveAttributes()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 根据数据获取外部信息
     */
    private fun getImageInfo(source: Map<*, *>, ex: ExifInterface): Map<String, String?> {
        val result = HashMap<String, String?>()
        source.keys.forEach { key ->
            val value = ex.getAttribute(key.toString())
            result[key.toString()] = value
        }
        return result
    }

    /**
     * 获取常用外部信息
     */
    private fun getExifCommonInfo(ex: ExifInterface): HashMap<String, String?> {
        val info = HashMap<String, String?>()
        info[ExifInterface.TAG_ORIENTATION] =
            ex.getAttribute(ExifInterface.TAG_ORIENTATION) //旋转角度，整形表示
        info[ExifInterface.TAG_DATETIME] =
            ex.getAttribute(ExifInterface.TAG_DATETIME) //拍摄时间，取决于设备设置的时间
        info[ExifInterface.TAG_MAKE] = ex.getAttribute(ExifInterface.TAG_MAKE) //设备品牌
        info[ExifInterface.TAG_MODEL] = ex.getAttribute(ExifInterface.TAG_MODEL) //设备型号
        info[ExifInterface.TAG_FLASH] = ex.getAttribute(ExifInterface.TAG_FLASH) //闪光灯
        info[ExifInterface.TAG_IMAGE_LENGTH] =
            ex.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) //图片高度
        info[ExifInterface.TAG_IMAGE_WIDTH] = ex.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) //图片宽度
        info[ExifInterface.TAG_GPS_LATITUDE] = ex.getAttribute(ExifInterface.TAG_GPS_LATITUDE) //纬度
        info[ExifInterface.TAG_GPS_LATITUDE] = ex.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) //经度
        info[ExifInterface.TAG_GPS_LATITUDE_REF] =
            ex.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) //纬度名（N or S）
        info[ExifInterface.TAG_GPS_LONGITUDE_REF] =
            ex.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) //经度名（E or W）
        info[ExifInterface.TAG_EXPOSURE_TIME] =
            ex.getAttribute(ExifInterface.TAG_EXPOSURE_TIME) //曝光时间
        info[ExifInterface.TAG_F_NUMBER] = ex.getAttribute(ExifInterface.TAG_F_NUMBER) //光圈值
        info[ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY] =
            ex.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY) //ISO感光度
        info[ExifInterface.TAG_DATETIME_DIGITIZED] =
            ex.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED) //数字化时间
        info[ExifInterface.TAG_GPS_ALTITUDE] =
            ex.getAttribute(ExifInterface.TAG_GPS_ALTITUDE) //海拔高度

        //指示用作参考高度的高度。 如果参考为海平面且海拔高于海平面，则给出0。 如果海拔高度低于海平面，则将值设置为1，并且在TAG_GPS_ALTITUDE中将海拔高度表示为绝对值
        info[ExifInterface.TAG_GPS_ALTITUDE_REF] =
            ex.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF)

        //gps时间戳 将时间表示为UTC（世界标准时间）。 时间戳表示为三个无符号有理值，分别表示小时，分钟和秒
        info[ExifInterface.TAG_GPS_TIMESTAMP] = ex.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP)

        //gps日期戳 记录相对于UTC（世界标准时间）的日期和时间信息的字符串。 格式为“ YYYY：MM：DD”
        info[ExifInterface.TAG_GPS_DATESTAMP] = ex.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)
        info[ExifInterface.TAG_WHITE_BALANCE] =
            ex.getAttribute(ExifInterface.TAG_WHITE_BALANCE) ////白平衡
        info[ExifInterface.TAG_FOCAL_LENGTH] = ex.getAttribute(ExifInterface.TAG_FOCAL_LENGTH) //焦距
        info[ExifInterface.TAG_GPS_PROCESSING_METHOD] =
            ex.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD)  //用于定位查找的全球定位系统处理方法。
        return info
    }
}