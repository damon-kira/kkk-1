

package com.kira.ui.core.extensions

fun <T> MutableList<T>.replaceList(collection: Collection<T>): List<T> {
    val temp = collection.toList()
    clear()
    addAll(temp)
    return this
}

fun <T> MutableList<T>.appendList(element: T): List<T> {
    if (!contains(element)) {
        add(element)
    }
    return this
}

// typealias
fun <T> Collection<T>.indexOf(predicate: (T) -> Boolean): Int {
    return indexOfFirst(predicate)
}

fun <T> Collection<T>.indexOrNull(predicate: (T) -> Boolean): Int? {
    val index = indexOfFirst(predicate)
    return if (index > -1) index else null
}