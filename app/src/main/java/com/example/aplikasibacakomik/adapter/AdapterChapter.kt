package com.example.aplikasibacakomik.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibacakomik.R
import com.example.aplikasibacakomik.model.ModelChapter

class AdapterChapter(
    context: Context,
    private val items: List<ModelChapter>,
    private val onSelectData: OnSelectData
) : RecyclerView.Adapter<AdapterChapter.ViewHolder>() {

    interface OnSelectData {
        fun onSelected(modelChapter: ModelChapter?)
    }

    private val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.list_item_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ModelChapter = items[position]
        holder.btnChapter.text = data.getChapterTitle()
        holder.btnChapter.setOnClickListener {
            onSelectData.onSelected(data)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Class Holder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llChapter: LinearLayout = itemView.findViewById(R.id.llChapter)
        var btnChapter: Button = itemView.findViewById(R.id.btnChapter)
    }
}
