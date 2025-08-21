package kira.learning.chat.util

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Clipboard manager for the platform that has capability to read the text from clipboard.
 */
class ClipboardManager(private val platformContext: PlatformContext) {
    /**
     * Returns the text from the clipboard.
     */
    suspend fun getClipboardText(): String? = withContext(Dispatchers.Default) {
        platformContext.getClipboardText()
    }
}

/**
 * Returns the [ClipboardManager] for the current platform in the current composition.
 */
@Composable
fun rememberClipboardManager(): ClipboardManager {
    val context = platformContext
    return remember { ClipboardManager(context) }
}

typealias PlatformContext = Application

val platformContext: PlatformContext
    @Composable
    @ReadOnlyComposable
    get() = LocalContext.current.applicationContext as PlatformContext

suspend fun PlatformContext.getClipboardText(): String? = runCatching {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = clipboard.primaryClip
    if (clip != null && clip.itemCount > 0) {
        val item = clip.getItemAt(0)
        return item.text.toString()
    }
    return null
}.getOrNull()
