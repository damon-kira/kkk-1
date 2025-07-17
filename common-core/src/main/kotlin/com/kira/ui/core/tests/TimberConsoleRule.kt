

package com.kira.ui.core.tests

import com.kira.ui.core.logger.ConsoleTree
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import timber.log.Timber

class TimberConsoleRule : TestWatcher() {

    private val consoleTree = ConsoleTree()

    override fun starting(description: Description) {
        Timber.plant(consoleTree)
    }

    override fun finished(description: Description) {
        Timber.uproot(consoleTree)
    }
}