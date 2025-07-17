

package com.kira.ui.feature.fonts.data.repository

import android.content.Context
import android.net.Uri
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.database.AppDatabase
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.fonts.data.converter.FontConverter
import com.kira.ui.feature.fonts.domain.model.FontModel
import com.kira.ui.feature.fonts.domain.model.InternalFont
import com.kira.ui.feature.fonts.domain.repository.FontsRepository
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class FontsRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val settingsManager: SettingsManager,
    private val appDatabase: AppDatabase,
    private val context: Context,
) : FontsRepository {

    override suspend fun current(): FontModel {
        return withContext(dispatcherProvider.io()) {
            val fontPath = settingsManager.fontType
            InternalFont.find(fontPath) ?: loadFont(fontPath)
        }
    }

    override suspend fun loadFonts(): List<FontModel> {
        return withContext(dispatcherProvider.io()) {
            val defaultFonts = InternalFont.values()
                .map(InternalFont::font)
            val userFonts = appDatabase.fontDao().loadAll()
                .map(FontConverter::toModel)
            userFonts + defaultFonts
        }
    }

    override suspend fun loadFonts(query: String): List<FontModel> {
        return withContext(dispatcherProvider.io()) {
            val defaultFonts = InternalFont.values()
                .map(InternalFont::font)
                .filter { it.fontName.contains(query, ignoreCase = true) }
            val userFonts = appDatabase.fontDao().loadAll()
                .map(FontConverter::toModel)
            userFonts + defaultFonts
        }
    }

    override suspend fun loadFont(path: String): FontModel {
        return withContext(dispatcherProvider.io()) {
            val fontEntity = appDatabase.fontDao().load(path)
            FontConverter.toModel(fontEntity)
        }
    }

    override suspend fun importFont(fileUri: Uri) {
        withContext(dispatcherProvider.io()) {
            context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val fontUuid = UUID.randomUUID().toString()
                val fontName = fileUri.path.orEmpty().substringAfterLast('/')
                val fontFile = File(context.cacheDir, fontUuid)
                if (!fontFile.exists()) {
                    fontFile.createNewFile()
                    inputStream.copyTo(fontFile.outputStream())
                }
                val fontModel = FontModel(
                    fontUuid = fontUuid,
                    fontName = fontName,
                    fontPath = fontFile.absolutePath,
                    isExternal = true,
                )
                appDatabase.fontDao().insert(FontConverter.toEntity(fontModel))
            }
        }
    }

    override suspend fun selectFont(fontModel: FontModel) {
        withContext(dispatcherProvider.io()) {
            settingsManager.fontType = fontModel.fontPath
        }
    }

    override suspend fun removeFont(fontModel: FontModel) {
        withContext(dispatcherProvider.io()) {
            val fontFile = File(context.cacheDir, fontModel.fontUuid)
            if (fontFile.exists()) {
                fontFile.deleteRecursively()
            }
            appDatabase.fontDao().delete(FontConverter.toEntity(fontModel))
            if (settingsManager.fontType == fontModel.fontPath) {
                settingsManager.remove(SettingsManager.KEY_FONT_TYPE)
            }
        }
    }
}