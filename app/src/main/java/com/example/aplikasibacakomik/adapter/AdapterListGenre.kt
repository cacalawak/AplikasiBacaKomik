package com.example.aplikasibacakomik.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasibacakomik.R
import com.example.aplikasibacakomik.model.ModelKomik


class AdapterListGenre(
    private val context: Context,
    private val items: List<ModelKomik>,
    private val onSelectData: onSelectData
) : RecyclerView.Adapter<AdapterListGenre.ViewHolder>() {

    interface onSelectData {
        fun onSelected(modelKomik: ModelKomik?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_komik, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ModelKomik = items[position]
        Glide.with(context)
            .load(data.getThumb())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.imgPhoto)
        holder.tvTitle.text = data.getTitle()
        holder.tvType.text = data.getType()
        holder.cvTerbaru.setOnClickListener {
            onSelectData.onSelected(data)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Class Holder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvType: TextView = itemView.findViewById(R.id.tvType)
        var cvTerbaru: CardView = itemView.findViewById(R.id.cvTerbaru)
        var imgPhoto: ImageView = itemView.findViewById(R.id.imgPhoto)
    }
}