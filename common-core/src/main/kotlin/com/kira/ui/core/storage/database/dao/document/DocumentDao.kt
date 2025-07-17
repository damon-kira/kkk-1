

package com.kira.ui.core.storage.database.dao.document

import androidx.room.Dao
import androidx.room.Query
import com.kira.ui.core.storage.database.dao.base.BaseDao
import com.kira.ui.core.storage.database.entity.document.DocumentEntity
import com.kira.ui.core.storage.database.utils.Tables

@Dao
abstract class DocumentDao : BaseDao<DocumentEntity> {

    @Query("SELECT * FROM `${Tables.DOCUMENTS}` ORDER BY `position` ASC")
    abstract suspend fun loadAll(): List<DocumentEntity>

    @Query("DELETE FROM `${Tables.DOCUMENTS}`")
    abstract suspend fun deleteAll()
}