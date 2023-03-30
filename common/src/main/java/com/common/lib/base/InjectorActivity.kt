package com.common.lib.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by weisl on 2019/9/24.
 */
open class InjectorActivity : AppCompatActivity() {

//    lateinit var viewModelFactory: ViewModelProvider.Factory

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> {
        return lazy {
            ViewModelProvider(this)[T::class.java]
        }
    }

//    @Inject
//    fun injectViewModelFactory(viewModelFactory: ViewModelFactory) {
//        if (this::viewModelFactory.isInitialized) return
//        this.viewModelFactory = viewModelFactory
//    }
}