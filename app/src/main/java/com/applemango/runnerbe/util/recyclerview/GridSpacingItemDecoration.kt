package com.applemango.runnerbe.util.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacingHorizontal: Int,
    private val spacingVertical: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spacingHorizontal - column * spacingHorizontal / spanCount
        outRect.right = (column + 1) * spacingHorizontal / spanCount
        outRect.bottom = spacingVertical
    }
}