package com.example.aplikasibacakomik.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.NonDisposableHandle.parent
import com.example.yourappname.R

class LayoutMarginDecoration(spacing: Int) : BaseLayoutMargin(1, spacing) {

    constructor(spanCount: Int, @Px spacing: Int) : super(spanCount, spacing)

    override fun setPadding(rv: RecyclerView, @Px margin: Int) {
        super.setPadding(rv, margin)
    }

    override fun setPadding(rv: RecyclerView, @Px top: Int, @Px bottom: Int, @Px left: Int, @Px right: Int) {
        super.setPadding(rv, top, bottom, left, right)
    }

    override fun setOnClickLayoutMarginItemListener(listener: OnClickLayoutMarginItemListener) {
        super.setOnClickLayoutMarginItemListener(listener)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val isRTL = parent.context.resources.getBoolean(R.bool.is_right_to_left)
        var orientation = OrientationHelper.VERTICAL
        var isInverse = false

        val position = parent.getChildAdapterPosition(view)

        var spanCurrent = position % spanCount
        if (parent.layoutManager is StaggeredGridLayoutManager) {
            val staggeredGridLayoutManager = parent.layoutManager as StaggeredGridLayoutManager
            orientation = staggeredGridLayoutManager.orientation
            isInverse = staggeredGridLayoutManager.reverseLayout
            val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
            spanCurrent = lp.spanIndex
        } else if (parent.layoutManager is GridLayoutManager) {
            val gridLayoutManager = parent.layoutManager as GridLayoutManager
            orientation = gridLayoutManager.orientation
            isInverse = gridLayoutManager.reverseLayout
            val lp = view.layoutParams as GridLayoutManager.LayoutParams
            spanCurrent = lp.spanIndex
            if (isRTL && orientation == OrientationHelper.VERTICAL) {
                spanCurrent = spanCount - spanCurrent - 1
            }
        } else if (parent.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = parent.layoutManager as LinearLayoutManager
            orientation = linearLayoutManager.orientation
            isInverse = linearLayoutManager.reverseLayout
            spanCurrent = 0
        }

        if (isRTL && orientation == OrientationHelper.HORIZONTAL) {
            spanCurrent = state.itemCount - position - 1
        }

        setupClickLayoutMarginItem(parent.context, view, position, spanCurrent, state)
        calculateMargin(
            outRect,
            position,
            spanCurrent,
            state.itemCount,
            orientation,
            isInverse,
            isRTL
        )
    }
}
