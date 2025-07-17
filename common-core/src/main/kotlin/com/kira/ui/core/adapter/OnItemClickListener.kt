package com.kira.ui.core.adapter

interface OnItemClickListener<in T> {
    fun onClick(item: T) = Unit
    fun onLongClick(item: T) = true
}