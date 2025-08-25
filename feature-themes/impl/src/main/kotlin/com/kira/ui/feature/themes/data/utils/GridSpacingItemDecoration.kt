

package com.kira.ui.feature.themes.data.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.common.kira.ui.dpToPx

class GridSpacingItemDecoration(
    marginDp: Int,
    private val columnCount: Int,
) : RecyclerView.ItemDecoration() {

    /**
     * In this algorithm space should divide by 3 without remnant or width of items can have
     * a difference and we want them to be exactly the same.
     */
    private val margin = (
        if (marginDp % 3 == 0) {
            marginDp
        } else {
            marginDp + (3 - marginDp % 3)
        }
    ).dpToPx()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        when {
            columnCount == 1 -> {
                outRect.left = margin
                outRect.right = margin
            }
            position % columnCount == 0 -> {
                outRect.left = margin
                outRect.right = margin / 3
            }
            position % columnCount == columnCount - 1 -> {
                outRect.right = margin
                outRect.left = margin / 3
            }
            else -> {
                outRect.left = margin * 2 / 3
                outRect.right = margin * 2 / 3
            }
        }
        if (position < columnCount) {
            outRect.top = margin
        }
        outRect.bottom = margin
    }
}