

package com.kira.ui.core.logger

import android.util.Log
import timber.log.Timber
import java.util.regex.Pattern

class ConsoleTree : Timber.DebugTree() {

    private val anonymousClass = Pattern.compile("""(\$\d+)+$""")

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val className = tag?.substringBefore('$', tag)
        val level = when (priority) {
            Log.VERBOSE -> 'V'
            Log.DEBUG -> 'D'
            Log.INFO -> 'I'
            Log.WARN -> 'W'
            Log.ERROR -> 'E'
            Log.ASSERT -> 'A'
            else -> '?'
        }
        println("$level/$className: $message")
    }

    override fun createStackElementTag(element: StackTraceElement): String {
        val matcher = anonymousClass.matcher(element.className)
        val tag = when {
            matcher.find() -> matcher.replaceAll("")
            else -> element.className
        }
        return tag.substringAfterLast('.')
    }
}