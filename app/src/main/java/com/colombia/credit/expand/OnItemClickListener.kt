package com.colombia.credit.expand

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.util.lib.log.logger_d


private const val TAG = "debug_OnItemClickListener"
interface OnItemClickListener {

    fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int)

    fun onLongItemClick(viewHolder: RecyclerView.ViewHolder, position: Int)
}

open class SimpleOnItemClickListener : OnItemClickListener {
    override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {}
    override fun onLongItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {}
}

fun RecyclerView.setOnItemClickListener(listener: OnItemClickListener) {
    addOnItemTouchListener(OnRecyclerItemClickListener(this, listener))
}

internal class OnRecyclerItemClickListener(
    private val recyclerView: RecyclerView,
    private val listener: OnItemClickListener
) : RecyclerView.OnItemTouchListener {

    private var mGestureDetector: GestureDetector? = null

    init {
        mGestureDetector =
            GestureDetector(
                recyclerView.context,
                ItemTouchHelperGestureListener(recyclerView, listener)
            )
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
        return mGestureDetector?.onTouchEvent(event) ?: false
    }

    override fun onTouchEvent(rv: RecyclerView, event: MotionEvent) {
//        mGestureDetector?.onTouchEvent(event)
    }

    override fun onRequestDisallowInterceptTouchEvent(p0: Boolean) {
    }

    private class ItemTouchHelperGestureListener(
        private val rv: RecyclerView,
        private val listener: OnItemClickListener
    ) : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            rv.findChildViewUnder(e.x, e.y)?.let { childView ->
                val viewHolder = rv.getChildViewHolder(childView)
                val position = rv.getChildAdapterPosition(childView)
                logger_d(TAG, "64 onSingleTapUp: position = $position")
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(viewHolder, position)
                    return true
                }
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            rv.findChildViewUnder(e.x, e.y)?.let { childView ->
                val viewHolder = rv.getChildViewHolder(childView)
                val position = rv.getChildAdapterPosition(childView)
                if(position != RecyclerView.NO_POSITION) {
                    listener.onLongItemClick(viewHolder, position)
                }
            }
        }
    }
}