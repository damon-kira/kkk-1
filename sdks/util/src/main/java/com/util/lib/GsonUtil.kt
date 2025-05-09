/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.util.lib


import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e


class GsonUtil {
    companion object {
        const val TAG = "GsonUtil"
        /**
         *
         * 描述：将对象转化为json.
         * @param
         * @return
         */
        fun toJson(src: Any): String? {
            var json: String? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                json = gson.toJson(src)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return json
        }

        /**
         *
         * 描述：将对象转化为jsonObject.
         * @param
         * @return
         */
        fun toJsonObject(src: Any): JsonObject? {
            var jsonObject: JsonObject? = null
            try {
                val gson = GsonBuilder().create()
                jsonObject = JsonParser.parseString(gson.toJson(src)).asJsonObject
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }

        /**
         *
         * 描述：将对象转化为jsonObject.
         * @param
         * @return
         */
        fun toJsonObject(src: String): JsonObject? {
            var jsonObject: JsonObject? = null
            try {
                jsonObject = JsonParser().parse(src).asJsonObject
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }

        /**
         *
         * 描述：将对象转化为jsonObject.这些字段需要使用注解@Expose修饰,没有使用@Expose的将不在json中
         * @param
         * @return
         */
        fun toJsonObjectByExcludeFields(src: Any): JsonObject? {
            var jsonObject: JsonObject? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.excludeFieldsWithoutExposeAnnotation().create()
                jsonObject = JsonParser().parse(gson.toJson(src)).asJsonObject
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }

        /**
         *
         * 描述：将列表转化为json.
         * @param list
         * @return
         */
        fun toJson(list: List<*>): String? {
            var json: String? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                json = gson.toJson(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return json
        }

        /**
         *
         * 描述：将json转化为列表.
         * @param json
         * @param typeToken object : TypeToken<ArrayList<*>>() {};
         * @return
         */
        fun <T> fromJson(json: String, typeToken: TypeToken<T>): T? {
            var result: T? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                val type = typeToken.type
                result = gson.fromJson<T>(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }

        /**
         *
         * 描述：将json转化为对象.
         * @param json
         * @param clazz
         * @return
         */
        fun fromJson(json: String, clazz: Class<*>): Any? {
            var obj: Any? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                obj = gson.fromJson(json, clazz)
            } catch (e: Exception) {
                if (isDebug()){
//                    MainHandler.post {
//                        toast(R.string.json_read_error)
//                    }
                }
                logger_e(TAG,"$e")
                e.printStackTrace()
            }

            return obj
        }

        /**
         *
         * 描述：将json转化为对象.
         * @param json
         * @param clazz
         * @return
         */
        fun <T> fromJsonNew(json: String, clazz: Class<T>): T? {
            var obj: T? = null
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                obj = gson.fromJson(json, clazz)
            } catch (e: Exception) {
                logger_e(TAG,"$e")
                e.printStackTrace()
            }

            return obj
        }

        /**
         *
         * 描述：将json转化为对象.
         * @param json
         * @param clazz
         * @return
         */
        inline fun <reified T> fromJsonNew(json: String): T? {
            try {
                val gsonb = GsonBuilder()
                val gson = gsonb.create()
                return gson.fromJson(json, T::class.java)
            } catch (e: Exception) {
                logger_e(TAG,"$e")
                e.printStackTrace()
            }

            return null
        }

        /**
         *
         * 描述：将对象转化为JsonArray
         * @param
         * @return
         */
        fun toJsonArray(src: String): JsonElement? {
            var jsonObject: JsonArray? = null
            try {
                jsonObject = JsonParser().parse(src).asJsonArray
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonObject
        }
    }
}
