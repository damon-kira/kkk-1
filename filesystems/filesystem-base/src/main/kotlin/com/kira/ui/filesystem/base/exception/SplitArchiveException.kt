

package com.kira.ui.filesystem.base.exception

class SplitArchiveException(path: String) : FilesystemException("Cannot open split archive ($path)")