

package com.kira.ui.feature.explorer.data.utils

import androidx.work.Data
import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.Permission
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val KEY_LIST = "list"
private const val KEY_FILE_URI = "fileUri"
private const val KEY_FILESYSTEM_UUID = "filesystemUuid"
private const val KEY_SIZE = "size"
private const val KEY_LAST_MODIFIED = "lastModified"
private const val KEY_DIRECTORY = "directory"
private const val KEY_PERMISSION = "permission"

internal fun FileModel.toData(): Data {
    return Data.Builder()
        .putString(KEY_FILE_URI, fileUri)
        .putString(KEY_FILESYSTEM_UUID, filesystemUuid)
        .putLong(KEY_SIZE, size)
        .putLong(KEY_LAST_MODIFIED, lastModified)
        .putBoolean(KEY_DIRECTORY, directory)
        .putInt(KEY_PERMISSION, permission)
        .build()
}

internal fun Data.toFileModel(): FileModel {
    return FileModel(
        fileUri = getString(KEY_FILE_URI).orEmpty(),
        filesystemUuid = getString(KEY_FILESYSTEM_UUID).orEmpty(),
        size = getLong(KEY_SIZE, 0L),
        lastModified = getLong(KEY_LAST_MODIFIED, 0L),
        directory = getBoolean(KEY_DIRECTORY, false),
        permission = getInt(KEY_PERMISSION, Permission.EMPTY),
    )
}

internal fun List<FileModel>.toData(): Data {
    return Data.Builder()
        .putString(KEY_LIST, Gson().toJson(this))
        .build()
}

internal fun Data.toFileList(): List<FileModel> {
    return Gson().fromJson(getString(KEY_LIST), object : TypeToken<List<FileModel>>() {}.type)
}