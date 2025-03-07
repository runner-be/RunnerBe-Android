package com.applemango.presentation.ui.screen.deco

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.applemango.presentation.util.dpToPx

class RecyclerViewHorizontalItemDeco(private val context : Context, private val height : Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount ?: (0 - 1))) {
            outRect.right = height.dpToPx(context)
        }
    }
}