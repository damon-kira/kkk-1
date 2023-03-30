package com.common.lib.viewbinding

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by weishl on 2023/3/24
 *
 */


//反射
inline fun <reified VB : ViewBinding> Fragment.bindView() = FragmentBindingDelegate(VB::class.java)

inline fun Fragment.doOnDestroyView(crossinline block: () -> Unit) {
    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                block.invoke()
            }
        }
    })
}

class FragmentBindingDelegate<VB : ViewBinding>(private val clazz: Class<VB>) :
    ReadOnlyProperty<Fragment, VB> {

    private var binding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (binding == null) {
            binding =
                clazz.getMethod("bind", View::class.java).invoke(null, thisRef.requireView()) as VB
            thisRef.doOnDestroyView { binding = null }
        }
        return binding!!
    }
}


inline fun <reified VB : ViewBinding> Fragment.binding(crossinline inflate: (LayoutInflater) -> VB) =
    lazy(LazyThreadSafetyMode.NONE) {
        var _binding: VB? = inflate(layoutInflater)
        val binding = _binding!!
        this.viewLifecycleOwner.lifecycle.addObserver(object: LifecycleEventObserver{
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    _binding = null
                }
            }
        })
        binding
    }

//不适用反射
inline fun <reified VB : ViewBinding> Fragment.bindView(noinline bind: (View) -> VB) =
    FragmentBindingDelegate2(bind)

class FragmentBindingDelegate2<VB : ViewBinding>(val bind: (View) -> VB) :
    ReadOnlyProperty<Fragment, VB> {

    private var mBinding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        mBinding = bind(thisRef.requireView())
        thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    mBinding = null
                }
            }
        })
        return mBinding!!
    }
}

