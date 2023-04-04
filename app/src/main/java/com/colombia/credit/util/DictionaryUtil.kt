package com.colombia.credit.util

import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.R

object DictionaryUtil {

    //获取联系人关系列表
    fun getRelationShip(): MutableMap<String, String> {
        return getMapData(R.array.relationShip_key, R.array.relationShip)
    }

    //获取婚姻状况
    @JvmStatic
    fun getMaritalData(): MutableMap<String, String> {
        return getMapData(R.array.marriage_key, R.array.marriage)
    }

    // 获取入职时间
    fun getEntryTimeData(): MutableMap<String, String> {
        return getMapData(R.array.entryTime_key, R.array.entryTime)
    }

    // 获取收入来源
    fun getIncomeSourceData(): MutableMap<String, String> {
        return getMapData(R.array.incomeSource_key, R.array.incomeSource)
    }

    // 获取教育程度列表
    fun getEducationData(): MutableMap<String, String> {
        return getMapData(R.array.education_key, R.array.education)
    }


    // 根据id获取数据列表
    private fun getStringArrayData(arrarId: Int): Array<String> {
        return getAppContext().resources.getStringArray(arrarId)
    }

    // 把list数据转化成map数据
    private fun produceMap(keyArray: Array<String>, value: Array<String>): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        for ((index, values) in value.withIndex()) {
            map[keyArray[index]] = values
        }
        return map
    }

    // 获取转化后的map数据
    private fun getMapData(keyId: Int, value: Int): MutableMap<String, String> {
        val value_array = getStringArrayData(value)
        val key_array = getStringArrayData(keyId)
        return produceMap(key_array, value_array)
    }
}