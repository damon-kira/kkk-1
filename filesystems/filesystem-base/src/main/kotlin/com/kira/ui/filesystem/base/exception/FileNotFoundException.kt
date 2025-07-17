

package com.kira.ui.filesystem.base.exception

class FileNotFoundException(path: String) : FilesystemException("No such file or directory ($path)")