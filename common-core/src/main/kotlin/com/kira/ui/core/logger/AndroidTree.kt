

package com.kira.ui.core.logger

import timber.log.Timber

class AndroidTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val className = tag?.substringBefore('$', tag)
        super.log(priority, className, message, t)
    }
}