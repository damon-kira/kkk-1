

package com.kira.ui.filesystem.base.exception

class FileAlreadyExistsException(path: String) : FilesystemException("Cannot create file $path because it's already exists")