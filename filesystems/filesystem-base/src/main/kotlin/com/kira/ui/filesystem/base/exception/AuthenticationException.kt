

package com.kira.ui.filesystem.base.exception

import com.kira.ui.filesystem.base.model.AuthMethod

class AuthenticationException(
    val authMethod: AuthMethod,
    val authError: Boolean
) : FilesystemException("Failed to authenticate on remote server")