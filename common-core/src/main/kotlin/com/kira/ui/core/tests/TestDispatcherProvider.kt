

package com.kira.ui.core.tests

import com.kira.ui.core.provider.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun computation(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun mainThread(): CoroutineDispatcher = Dispatchers.Unconfined
}