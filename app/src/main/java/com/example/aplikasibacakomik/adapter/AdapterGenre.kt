package com.example.aplikasibacakomik.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibacakomik.R
import com.example.aplikasibacakomik.model.ModelGenre

class AdapterGenre(context: Context, items: List<ModelGenre>, xSelectData: onSelectData) :
    RecyclerView.Adapter<AdapterGenre.ViewHolder>() {

    private val items: List<ModelGenre>
    private val onSelectData: onSelectData
    private val mContext: Context

    interface OnSelectData {
        fun onSelected(modelGenre: ModelGenre?)
    }

    init {
        mContext = context
        this.items = items
        onSelectData = xSelectData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_genre, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ModelGenre = items[position]
        holder.tvGenre.text = data.getTitle()
        holder.llGenre.setOnClickListener {
            onSelectData.onSelected(data)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Class Holder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llGenre: LinearLayout = itemView.findViewById(R.id.llGenre)
        var tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
    }
}
