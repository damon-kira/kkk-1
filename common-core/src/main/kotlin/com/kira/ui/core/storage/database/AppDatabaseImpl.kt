package com.kira.ui.core.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kira.learning.modules.videoplayer.data.dao.VideoNoteDao
import com.kira.learning.modules.videoplayer.data.dao.VideoProgressDao
import com.kira.learning.modules.videoplayer.models.VideoNoteEntity
import com.kira.learning.modules.videoplayer.models.VideoProgressEntity
import com.kira.ui.core.storage.database.dao.document.DocumentDao
import com.kira.ui.core.storage.database.dao.font.FontDao
import com.kira.ui.core.storage.database.dao.server.ServerDao
import com.kira.ui.core.storage.database.dao.theme.ThemeDao
import com.kira.ui.core.storage.database.entity.document.DocumentEntity
import com.kira.ui.core.storage.database.entity.font.FontEntity
import com.kira.ui.core.storage.database.entity.server.ServerEntity
import com.kira.ui.core.storage.database.entity.theme.ThemeEntity

@Database(
    entities = [
        DocumentEntity::class,
        ServerEntity::class,
        FontEntity::class,
        ThemeEntity::class,
        VideoNoteEntity::class,
        VideoProgressEntity::class,
    ],
    version = 6,
)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {

    companion object {
        const val DATABASE_NAME = "database"
    }

    abstract override fun documentDao(): DocumentDao
    abstract override fun serverDao(): ServerDao
    abstract override fun fontDao(): FontDao
    abstract override fun themeDao(): ThemeDao
    abstract fun videoNoteDao(): VideoNoteDao
    abstract fun videoProgressDao(): VideoProgressDao

    override fun shutdown() {
        clearAllTables()
    }
}