package com.util.lib.shape

import android.graphics.drawable.GradientDrawable
import android.view.View

abstract class BaseParams(protected val iShape: IShape) {

    fun then(): IShape {
        return iShape
    }

    fun getShape(): GradientDrawable {
        return iShape.getShape()
    }

    fun into(view: View) {
        view.background = getShape()
    }
}