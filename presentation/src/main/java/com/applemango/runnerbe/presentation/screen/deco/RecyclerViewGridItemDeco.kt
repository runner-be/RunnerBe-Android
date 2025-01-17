package com.applemango.runnerbe.presentation.screen.deco

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.util.dpToPx

class RecyclerViewGridItemDeco(
    private val context: Context,
    private val horizonSpace: Int,
    private val verticalSpace : Int,
    private val includeEdge: Boolean,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            outRect.left =
                (horizonSpace - column * horizonSpace / spanCount).dpToPx(context) // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                ((column + 1) * horizonSpace / spanCount).dpToPx(context)// (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = verticalSpace.dpToPx(context)
            }
            outRect.bottom = verticalSpace.dpToPx(context) // item bottom
        } else {
            outRect.left = (column * horizonSpace / spanCount).dpToPx(context) // column * ((1f / spanCount) * spacing)
            outRect.right =
                (horizonSpace - (column + 1) * horizonSpace / spanCount).dpToPx(context) // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = verticalSpace.dpToPx(context) // item top
            }
        }
    }
}