package com.common.lib.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by weishl on 2023/3/24
 *
 */
inline fun <reified VB : ViewBinding> newBindingViewHolder(parent: ViewGroup): BindingViewHolder<VB> {
    val method = VB::class.java.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    val binding = method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
    return BindingViewHolder(binding)
}


fun <VB : ViewBinding> BindingViewHolder<VB>.withBinding(block: VB.(BindingViewHolder<VB>) -> Unit) =
    kotlin.run {
        block(binding, this@withBinding)
    }

fun <VB : ViewBinding> RecyclerView.ViewHolder.getBinding(bind: (View) -> VB) =
    bind(itemView)

fun <VB : ViewBinding> RecyclerView.ViewHolder.withBinding(bind: (View) -> VB, block: VB.(RecyclerView.ViewHolder) -> Unit)= kotlin.run {
    block(bind(itemView), this@withBinding)
}


class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
    constructor(parent: ViewGroup, inflater: (LayoutInflater, ViewGroup, Boolean) -> VB) :
            this(inflater(LayoutInflater.from(parent.context), parent, false))
}
