package com.kira.learning.module.ai

import android.location.Address
import androidx.room.TypeConverter
import com.kira.learning.bean.dao.Choice
import com.kira.learning.bean.dao.ChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChoiceConverters {
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Choice>>() {}.type

    // 将 List<Choice> 转换为 JSON 字符串
    @TypeConverter
    fun fromChoiceList(choices: List<Choice>): String {
        return gson.toJson(choices, typeToken)
    }

    // 将 JSON 字符串转换为 List<Choice>
    @TypeConverter
    fun toChoiceList(json: String): List<Choice> {
        return gson.fromJson(json, typeToken)
    }

    // 单独处理 Message 对象（如果需要直接操作）
    @TypeConverter
    fun fromMessage(message: ChatMessage): String {
        return gson.toJson(message)
    }

    @TypeConverter
    fun toMessage(json: String): ChatMessage {
        return gson.fromJson(json, ChatMessage::class.java)
    }
}

class ArrayListConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun toList(json: String): List<String> =
        Gson().fromJson(json, Array<String>::class.java).toList()
}

class TypeConverter {
    @TypeConverter
    fun fromAddress(address: Address): String = Gson().toJson(address)

    @TypeConverter
    fun toAddress(json: String): Address = Gson().fromJson(json, Address::class.java)
}