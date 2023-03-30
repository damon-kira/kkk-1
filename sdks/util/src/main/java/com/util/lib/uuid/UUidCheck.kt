package com.util.lib.uuid

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import com.util.lib.BuildConfig
import com.util.lib.FileUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * Created by weishl on 2022/1/25
 *
 */
object UUidCheck {

    private const val INVALID_IMEI_FILENAME = "non_imei"
    private val DEBUG = BuildConfig.DEBUG
    private const val TAG = "debug_UUidCheck"

    /**
     * @return Long返回0 UUID无效 否则返回存储时的时间戳
     */
    fun checkUUidValid(uuid1: String, uuid2: String): Pair<String, Long> {
        var uuid1Pair: Pair<String, Long> = Pair("", 0)
        var uuid2Pair: Pair<String, Long> = Pair("", 0)
        var finalPair: Pair<String, Long> = Pair("", 0)
        if (uuid1.isNotEmpty()) {
            val uuids = uuid1.split("+")
            val time = getTime(uuids)
            uuid1Pair = if (time == 0L) {
                Pair(uuids[0], 0)
            } else
                Pair(uuids[0], time)
        }
        if (uuid2.isNotEmpty()) {
            val uuids = uuid2.split("+")
            val time = getTime(uuids)
            uuid2Pair = if (time == 0L) {
                Pair(uuids[0], 0)
            } else
                Pair(uuids[0], time)
        }


        val time1 = uuid1Pair.second
        val time2 = uuid2Pair.second
        if (time1 > 0 && time2 > 0) {
            if (time1 < time2) {
                finalPair = uuid1Pair
            } else uuid2Pair
        } else if (time1 > 0) {
            finalPair = uuid1Pair
        } else if (time2 > 0) {
            finalPair = uuid2Pair
        }
        return finalPair
    }

    private fun getTime(strs: List<String>): Long {
        try {
            return if (strs.size == 2) {
                strs[1] as Long
            } else 0L
        } catch (e: Exception) {
        }
        return 0L
    }


    @WorkerThread
    fun invalidCashDeviceId(context: Context, str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return true
        }
        var iso: InputStream? = null
        var br: BufferedReader? = null
        try {
            iso = FileUtils.openLatestInputFile(context,
                INVALID_IMEI_FILENAME
            )
            if (iso != null) {
                br = BufferedReader(InputStreamReader(iso))
                var regexp: String? = br.readLine()
                while (regexp != null) {
                    try {
                        val pattern = Pattern.compile(regexp)
                        val match = pattern.matcher(str)
                        if (match.matches()) {
                            return true
                        }
                        regexp = br.readLine()
                    } catch (ex: Exception) {
                        if (DEBUG) {
                            Log.e(TAG, ex.toString())
                        }
                    }

                }
            }
        } catch (ex: Exception) {
            if (DEBUG) {
                Log.e(TAG, ex.toString())
            }
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    if(DEBUG) {
                        Log.e(TAG, e.toString())
                    }
                }

            }
            if (iso != null) {
                try {
                    iso.close()
                } catch (ex: Exception) {
                    if (DEBUG) {
                        Log.e(TAG, ex.toString())
                    }
                }

            }
        }
        return false
    }
}