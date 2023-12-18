package com.example.aplikasibacakomik.utils

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnClickLayoutMarginItemListener {
    fun onClick(context: Context, v: View, position: Int, spanIndex: Int, state: RecyclerView.State)
}
