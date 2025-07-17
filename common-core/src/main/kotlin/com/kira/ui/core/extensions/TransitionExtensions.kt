

package com.kira.ui.core.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.ViewGroupCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough

fun Fragment.postponeEnterTransition(view: View) {
    postponeEnterTransition()
    view.doOnPreDraw {
        startPostponedEnterTransition()
    }
}

fun Fragment.setFadeTransition(viewGroup: ViewGroup, @IdRes vararg excludeIds: Int) {
    enterTransition = MaterialFadeThrough().apply {
        excludeIds.forEach { excludeTarget(it, true) }
    }
    exitTransition = MaterialFadeThrough().apply {
        excludeIds.forEach { excludeTarget(it, true) }
    }
    ViewGroupCompat.setTransitionGroup(viewGroup, true)
}