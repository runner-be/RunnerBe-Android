package com.applemango.presentation.util

import android.view.View
import androidx.core.widget.NestedScrollView
import com.sothree.slidinguppanel.ScrollableViewHelper

class NestedScrollableViewHelper(private val mScrollView: NestedScrollView) : ScrollableViewHelper() {
    override fun getScrollableViewScrollPosition(scrollableView: View, isSlidingUp: Boolean): Int {
        return if (isSlidingUp) {
            mScrollView.scrollY
        } else {
            val child = mScrollView.getChildAt(0)
            child.bottom - (mScrollView.height + mScrollView.scrollY)
        }
    }
}