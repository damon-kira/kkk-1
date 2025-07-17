package com.kira.learning.module.supereditor.internal.provider.coroutine

import com.kira.ui.core.provider.coroutine.DispatcherProvider
import kotlinx.coroutines.Dispatchers

class DispatcherProviderImpl : DispatcherProvider {
    override fun io() = Dispatchers.IO
    override fun computation() = Dispatchers.Default
    override fun mainThread() = Dispatchers.Main
}