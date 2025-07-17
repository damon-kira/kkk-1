

package com.kira.ui.core.storage

import android.content.Context
import java.io.File

object Directories {

    fun migrateFilenames(context: Context) {
        val directory = filesDir(context)
        directory.listFiles()?.forEach { file ->
            if (file.extension != ".txt") {
                val newFile = File(file.parent, file.nameWithoutExtension + ".txt")
                file.renameTo(newFile)
            }
        }
    }

    /** /data/data/com.kira.ui/files */
    fun filesDir(context: Context): File {
        val directory = File(context.dataDir, "files")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    /** /data/data/com.kira.ui/ftp */
    fun ftpDir(context: Context): File {
        val directory = File(context.dataDir, "ftp")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }
}