

package com.kira.ui.core.storage.database.dao.server

import androidx.room.Dao
import androidx.room.Query
import com.kira.ui.core.storage.database.dao.base.BaseDao
import com.kira.ui.core.storage.database.entity.server.ServerEntity
import com.kira.ui.core.storage.database.utils.Tables
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ServerDao : BaseDao<ServerEntity> {

    @Query("SELECT * FROM `${Tables.SERVERS}`")
    abstract fun flow(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM `${Tables.SERVERS}`")
    abstract suspend fun loadAll(): List<ServerEntity>

    @Query("SELECT * FROM `${Tables.SERVERS}` WHERE `uuid` = :uuid")
    abstract suspend fun load(uuid: String): ServerEntity

    @Query("DELETE FROM `${Tables.SERVERS}`")
    abstract suspend fun deleteAll()
}