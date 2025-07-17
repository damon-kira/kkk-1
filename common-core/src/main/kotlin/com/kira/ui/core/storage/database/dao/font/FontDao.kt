

package com.kira.ui.core.storage.database.dao.font

import androidx.room.Dao
import androidx.room.Query
import com.kira.ui.core.storage.database.dao.base.BaseDao
import com.kira.ui.core.storage.database.entity.font.FontEntity
import com.kira.ui.core.storage.database.utils.Tables

@Dao
abstract class FontDao : BaseDao<FontEntity> {

    @Query("SELECT * FROM `${Tables.FONTS}`")
    abstract suspend fun loadAll(): List<FontEntity>

    @Query("SELECT * FROM `${Tables.FONTS}` WHERE `font_name` LIKE '%' || :searchQuery || '%'")
    abstract suspend fun loadAll(searchQuery: String): List<FontEntity>

    @Query("SELECT * FROM `${Tables.FONTS}` WHERE `font_path` = :path")
    abstract suspend fun load(path: String): FontEntity // TODO select by uuid

    @Query("DELETE FROM `${Tables.FONTS}`")
    abstract suspend fun deleteAll()
}