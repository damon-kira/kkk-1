package com.common.lib.viewbinding

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


inline fun <reified VB : ViewBinding> ComponentActivity.binding() =
    lazy {
        inflateBinding<VB>(layoutInflater).also {
            setContentView(it.root)
        }
    }

inline fun <reified VB : ViewBinding> Dialog.binding() = lazy {
    inflateBinding<VB>(layoutInflater).also { setContentView(it.root) }
}

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater): VB {
    return VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB
}

inline fun <reified VB : ViewBinding> ComponentActivity.binding(crossinline inflate: (LayoutInflater) -> VB) =
    lazy(LazyThreadSafetyMode.NONE) {
        inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

inline fun <reified VB : ViewBinding> Dialog.binding(crossinline inflate: (LayoutInflater) -> VB) =
    lazy(LazyThreadSafetyMode.NONE) {
        inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }
