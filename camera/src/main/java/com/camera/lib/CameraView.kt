package com.camera.lib

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.view.PreviewView


class CameraView : FrameLayout {

    private var cameraType = CameraType.CameraX

     var curView: View = PreviewView(context)

    val params = LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        curView.layoutParams = params
        addView(curView)
    }

    fun setCameraType(cameraType: CameraType){
        if (cameraType == this.cameraType){
            return
        }
        when(cameraType){
            CameraType.CameraOne -> {
                removeAllViews()
                curView = TextureView(context)
                curView.layoutParams = params
                addView(curView)
            }
            CameraType.CameraX -> {
                removeAllViews()
                curView = PreviewView(context)
                curView.layoutParams = params
                addView(curView)
            }
        }
    }
}