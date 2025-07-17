package com.kira.ui.feature.explorer.domain.factory

import com.kira.ui.filesystem.base.Filesystem

interface FilesystemFactory {
    suspend fun create(uuid: String): Filesystem
}