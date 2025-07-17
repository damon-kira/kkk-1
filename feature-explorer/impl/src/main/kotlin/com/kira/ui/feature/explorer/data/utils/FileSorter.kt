

package com.kira.ui.feature.explorer.data.utils

import com.kira.ui.filesystem.base.model.FileModel
import kotlin.Comparator

object FileSorter {

    const val SORT_BY_NAME = 0
    const val SORT_BY_SIZE = 1
    const val SORT_BY_DATE = 2

    val COMPARATOR_NAME: Comparator<in FileModel>
        get() = Comparator { first, second ->
            first.name.compareTo(second.name, ignoreCase = true)
        }

    val COMPARATOR_SIZE: Comparator<in FileModel>
        get() = Comparator { first, second ->
            second.size.compareTo(first.size)
        }

    val COMPARATOR_DATE: Comparator<in FileModel>
        get() = Comparator { first, second ->
            second.lastModified.compareTo(first.lastModified)
        }
}

fun fileComparator(sortMode: Int): Comparator<in FileModel> {
    return when (sortMode) {
        FileSorter.SORT_BY_NAME -> FileSorter.COMPARATOR_NAME
        FileSorter.SORT_BY_SIZE -> FileSorter.COMPARATOR_SIZE
        FileSorter.SORT_BY_DATE -> FileSorter.COMPARATOR_DATE
        else -> throw IllegalArgumentException("Unknown sort type")
    }
}