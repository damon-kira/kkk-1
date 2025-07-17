

package com.kira.ui.core.storage.database.entity.document

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kira.ui.core.storage.database.utils.Tables

@Entity(tableName = Tables.DOCUMENTS)
data class DocumentEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: String,
    // TODO rename column to 'file_uri'
    @ColumnInfo(name = "path")
    val fileUri: String,
    @ColumnInfo(name = "filesystem_uuid")
    val filesystemUuid: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "modified")
    val modified: Boolean,
    @ColumnInfo(name = "position")
    val position: Int,
    @ColumnInfo(name = "scroll_x")
    val scrollX: Int,
    @ColumnInfo(name = "scroll_y")
    val scrollY: Int,
    @ColumnInfo(name = "selection_start")
    val selectionStart: Int,
    @ColumnInfo(name = "selection_end")
    val selectionEnd: Int,
)