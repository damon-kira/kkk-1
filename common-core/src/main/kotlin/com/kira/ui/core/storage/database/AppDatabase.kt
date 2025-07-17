

package com.kira.ui.core.storage.database

import com.kira.ui.core.storage.database.dao.document.DocumentDao
import com.kira.ui.core.storage.database.dao.font.FontDao
import com.kira.ui.core.storage.database.dao.server.ServerDao
import com.kira.ui.core.storage.database.dao.theme.ThemeDao

interface AppDatabase {
    fun documentDao(): DocumentDao
    fun serverDao(): ServerDao
    fun fontDao(): FontDao
    fun themeDao(): ThemeDao
    fun shutdown()
}