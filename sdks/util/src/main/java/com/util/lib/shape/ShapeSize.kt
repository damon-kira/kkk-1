package com.util.lib.shape

class ShapeSize(iShape: IShape): BaseParams(iShape) {

    fun size(width: Int, height: Int): ShapeSize  {
        iShape.getShape().setSize(width, height)
        return this
    }
}