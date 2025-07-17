

package com.kira.ui.core.storage.database.entity.font

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kira.ui.core.storage.database.utils.Tables

@Entity(tableName = Tables.FONTS)
data class FontEntity(
    @ColumnInfo(name = "font_uuid")
    val fontUuid: String,
    @ColumnInfo(name = "font_name")
    val fontName: String,
    @PrimaryKey
    @ColumnInfo(name = "font_path")
    val fontPath: String,
    @Deprecated("Legacy column")
    @ColumnInfo(name = "support_ligatures")
    val supportLigatures: Boolean,
)