

package com.kira.ui.filesystem.base.exception

class UnsupportedArchiveException(path: String) : FilesystemException("Cannot open unsupported archive ($path)")