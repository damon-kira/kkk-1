package com.common.lib.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Create by weishl
 * 2022/9/1
 */
abstract class InjectorFragment : Fragment() {

//    constructor(layoutId: Int):super(layoutId)

//    lateinit var viewModelFactory: ViewModelProvider.Factory

//    @Inject
//    fun injectViewModelFactory(viewModelFactory: ViewModelFactory) {
//        if (this::viewModelFactory.isInitialized) return
//        this.viewModelFactory = viewModelFactory
//    }

    inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> {
        return lazy { ViewModelProvider(this)[T::class.java] }
    }
}