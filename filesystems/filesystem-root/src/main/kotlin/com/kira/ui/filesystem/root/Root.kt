

package com.kira.ui.filesystem.root

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val TIMEOUT = 20_000L

fun requestRootAccess(): Shell {
    return runBlocking {
        withTimeout(TIMEOUT) {
            suspendCoroutine { cont ->
                try {
                    cont.resume(Shell.getShell())
                } catch (e: Throwable) {
                    cont.resumeWithException(e)
                }
            }
        }
    }
}