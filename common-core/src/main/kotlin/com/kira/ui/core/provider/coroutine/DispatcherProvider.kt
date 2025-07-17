

package com.kira.ui.core.provider.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun computation(): CoroutineDispatcher
    fun mainThread(): CoroutineDispatcher
}