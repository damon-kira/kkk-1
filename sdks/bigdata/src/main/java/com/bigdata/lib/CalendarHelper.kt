package com.bigdata.lib

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.bigdata.lib.net.BigDataNetBaseParamsManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.io.IOException


/**
 * Created by weishl on 2020/12/31
 *
 */
object CalendarHelper {

    private const val TAG = "debug_CalendarHelper"
    /**
     * 获取app日历 event
     */
    fun getCalendarEvent(context: Context): JsonArray {
        val jsonArray = JsonArray()
        if (BigDataNetBaseParamsManager.isPermissionAuth(context, Manifest.permission.READ_CALENDAR) == 0){
            logger_i(TAG, " 日历 = 没有权限")
            return jsonArray
        }
        val config = BigDataManager.get().getNetDataListener()
        if (config!=null){

            var eventCursor: Cursor? = null
            try {
                val uri = Uri.parse("content://com.android.calendar/events")
                eventCursor = config.getContext().contentResolver.query(
                    uri,
                    arrayOf("_id", "title", "description", "dtstart", "dtend"),
                    null,
                    null,
                    CalendarContract.Events.DTSTART + " DESC limit 100"
                )
                var jsonObject: JsonObject? = null
                if (eventCursor != null) {

                    while (eventCursor.moveToNext()) {

                        jsonObject = JsonObject()
                        jsonObject.addProperty(
                            "event_id", eventCursor.getString(0).orEmpty()
                        )
                        jsonObject.addProperty(
                            "event_title", eventCursor.getString(1).orEmpty()
                        )
                        jsonObject.addProperty(
                            "description", eventCursor.getString(2).orEmpty()
                        )
                        jsonObject.addProperty(
                            "start_time", eventCursor.getString(3).orEmpty()
                        )
                        jsonObject.addProperty(
                            "end_time", eventCursor.getString(4).orEmpty()
                        )

                        val eventId = jsonObject["event_id"].asString

                        val canAdd = getCalendaReminders(jsonObject, eventId)
                        if (canAdd) {
                            jsonArray.add(jsonObject)
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                logger_e(TAG, "calendar exception = $e")
            } finally {
                close(eventCursor)
            }
            logger_i(TAG, "日历 。。。$jsonArray")
        }

        return jsonArray
    }


    /**
     * 获取app日历 event 提醒
     */
    private fun getCalendaReminders(jsonObjectParams: JsonObject, eventId: String): Boolean {
        var eventCursor: Cursor? = null
        val config = BigDataManager.get().getNetDataListener()
        if (config!=null){

            val jsonArray = JsonArray()
            try {
                val uri = Uri.parse("content://com.android.calendar/reminders")
                eventCursor = config.getContext().contentResolver.query(
                    uri,
                    arrayOf("_id", "event_id", "minutes", "method"),
                    CalendarContract.Reminders.EVENT_ID + "=?",
                    arrayOf(eventId),
                    null
                )
                var jsonObject: JsonObject? = null
                if (eventCursor != null) {
                    if (eventCursor.count > 0) {
                        while (eventCursor.moveToNext()) {

                            jsonObject = JsonObject()

                            jsonObject.addProperty(
                                "reminder_id", eventCursor.getString(0).orEmpty()
                            )
                            jsonObject.addProperty(
                                "event_id", eventCursor.getString(1).orEmpty()
                            )
                            jsonObject.addProperty(
                                "minutes", eventCursor.getString(2).orEmpty()
                            )
                            jsonObject.addProperty(
                                "method", eventCursor.getString(3).orEmpty()
                            )

                            jsonArray.add(jsonObject)

                        }
                        jsonObjectParams.add("reminders", jsonArray)
                        return true
                    }
                }

            } catch (e: java.lang.Exception) {

            } finally {
                close(eventCursor)
            }

        }
        return false
    }

}

fun close(cursor: Cursor?): Boolean {
    try {
        cursor?.close()
        return true
    } catch (e: IOException) {
    }
    return false
}