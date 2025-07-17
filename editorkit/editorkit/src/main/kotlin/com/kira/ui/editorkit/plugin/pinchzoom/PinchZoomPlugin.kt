

package com.kira.ui.editorkit.plugin.pinchzoom

import android.util.Log
import android.view.MotionEvent
import com.kira.ui.editorkit.plugin.base.EditorPlugin
import com.kira.ui.editorkit.widget.TextProcessor
import kotlin.math.ceil
import kotlin.math.sqrt

class PinchZoomPlugin : EditorPlugin(PLUGIN_ID) {

    var minTextSize = DEFAULT_MIN_TEXT_SIZE
    var maxTextSize = DEFAULT_MAX_TEXT_SIZE

    private var isDoingPinchZoom = false
    private var pinchFactor = 1f

    override fun onAttached(editText: TextProcessor) {
        super.onAttached(editText)
        Log.d(PLUGIN_ID, "PinchZoom plugin loaded successfully!")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> isDoingPinchZoom = false
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 2) {
                    val distance = getDistanceBetweenTouches(event)
                    if (!isDoingPinchZoom) {
                        val scaledDensity = requireContext()
                            .resources.displayMetrics.scaledDensity
                        val textSize = editText.textSize

                        pinchFactor = textSize / scaledDensity / distance
                        isDoingPinchZoom = true
                    }
                    return updateTextSize(pinchFactor * distance)
                }
            }
        }
        return false
    }

    private fun getDistanceBetweenTouches(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    private fun updateTextSize(size: Float): Boolean {
        editText.textSize = when {
            size < minTextSize -> minTextSize
            size > maxTextSize -> maxTextSize
            else -> ceil(size * 2) / 2 // step 0.5
        }
        return true
    }

    companion object {

        const val PLUGIN_ID = "pinchzoom-0361"

        const val DEFAULT_MIN_TEXT_SIZE = 10f
        const val DEFAULT_MAX_TEXT_SIZE = 20f
    }
}