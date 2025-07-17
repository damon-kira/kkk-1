

package com.kira.ui.filesystem.base.exception

class EncryptedArchiveException(path: String) : FilesystemException("Cannot open encrypted archive ($path)")