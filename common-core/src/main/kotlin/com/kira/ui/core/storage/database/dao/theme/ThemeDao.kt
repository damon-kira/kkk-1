

package com.kira.ui.core.storage.database.dao.theme

import androidx.room.Dao
import androidx.room.Query
import com.kira.ui.core.storage.database.dao.base.BaseDao
import com.kira.ui.core.storage.database.entity.theme.ThemeEntity
import com.kira.ui.core.storage.database.utils.Tables

@Dao
abstract class ThemeDao : BaseDao<ThemeEntity> {

    @Query("SELECT * FROM `${Tables.THEMES}`")
    abstract suspend fun loadAll(): List<ThemeEntity>

    @Query("SELECT * FROM `${Tables.THEMES}` WHERE `name` LIKE '%' || :searchQuery || '%'")
    abstract suspend fun loadAll(searchQuery: String): List<ThemeEntity>

    @Query("SELECT * FROM `${Tables.THEMES}` WHERE `uuid` = :uuid")
    abstract suspend fun load(uuid: String): ThemeEntity

    @Query("DELETE FROM `${Tables.THEMES}`")
    abstract suspend fun deleteAll()
}