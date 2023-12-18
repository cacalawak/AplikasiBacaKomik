package com.example.aplikasibacakomik.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.View.SCROLLBARS_OUTSIDE_OVERLAY
import androidx.annotation.NonNull
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

abstract class BaseLayoutMargin(spanCount: Int, @Px spacing: Int) : RecyclerView.ItemDecoration() {
    private val marginDelegate: MarginDelegate = MarginDelegate(spanCount, spacing)
    private val spanCount: Int = spanCount
    private val spacing: Int = spacing
    private var listener: OnClickLayoutMarginItemListener? = null

    fun setOnClickLayoutMarginItemListener(listener: OnClickLayoutMarginItemListener) {
        this.listener = listener
    }

    fun setPadding(rv: RecyclerView, @Px margin: Int) {
        setPadding(rv, margin, margin, margin, margin)
    }

    fun setPadding(rv: RecyclerView, @Px top: Int, @Px bottom: Int, @Px left: Int, @Px right: Int) {
        rv.clipToPadding = false
        rv.scrollBarStyle = SCROLLBARS_OUTSIDE_OVERLAY
        rv.setPadding(left, top, right, bottom)
    }

    fun getMarginDelegate(): MarginDelegate {
        return marginDelegate
    }

    fun calculateMargin(
        outRect: Rect,
        position: Int,
        spanCurrent: Int,
        itemCount: Int,
        orientation: Int,
        isReverse: Boolean,
        isRTL: Boolean
    ) {
        marginDelegate.calculateMargin(outRect, position, spanCurrent, itemCount, orientation, isReverse, isRTL)
    }

    fun getSpacing(): Int {
        return spacing
    }

    fun getSpanCount(): Int {
        return spanCount
    }

    fun setupClickLayoutMarginItem(
        context: Context,
        view: View,
        position: Int,
        spanCurrent: Int,
        state: RecyclerView.State
    ) {
        if (listener != null)
            view.setOnClickListener(onClickItem(context, view, position, spanCurrent, state))
    }

    @NonNull
    private fun onClickItem(
        context: Context,
        view: View,
        position: Int,
        currentSpan: Int,
        state: RecyclerView.State
    ): View.OnClickListener {
        return View.OnClickListener {
            if (listener != null)
                listener?.onClick(context, view, position, currentSpan, state)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
    }
}
