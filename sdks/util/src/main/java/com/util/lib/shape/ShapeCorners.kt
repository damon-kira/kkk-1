package com.util.lib.shape

class ShapeCorners(iShape: IShape): BaseParams(iShape) {

    fun radius(radius: Float): ShapeCorners {
        iShape.getShape().cornerRadius = radius
        return this
    }

    fun radius(radius: Int): ShapeCorners {
        iShape.getShape().cornerRadius = radius.toFloat()
        return this
    }

    fun radius(radius: FloatArray): ShapeCorners {
        iShape.getShape().cornerRadii = radius
        return this
    }

    fun radius(
        leftTop: Float,
        rightTop: Float,
        rightBottom: Float,
        leftBottom: Float
    ): ShapeCorners {
        iShape.getShape().cornerRadii = floatArrayOf(
            leftTop,
            leftTop,
            rightTop,
            rightTop,
            rightBottom,
            rightBottom,
            leftBottom,
            leftBottom
        )
        return this
    }
}