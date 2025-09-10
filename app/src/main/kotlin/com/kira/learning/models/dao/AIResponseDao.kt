package com.kira.learning.models.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AIResponseDao {

    // 插入一条响应数据
    @Insert
    suspend fun insert(response: AIResponseInfo)

    // 查询所有响应数据
    @Query("SELECT * FROM ai_responses")
    fun getAllResponses(): Flow<List<AIResponseInfo>>

    // 根据 ID 删除响应
    @Query("DELETE FROM ai_responses WHERE idKey = :id")
    suspend fun deleteById(id: Long)

    // 使用事务批量插入
    @Transaction
    suspend fun batchInsert(responses: List<AIResponseInfo>) {
        responses.forEach { insert(it) }
    }
}