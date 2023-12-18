package com.example.aplikasibacakomik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasibacakomik.R
import com.example.aplikasibacakomik.model.ModelSlider


class AdapterSlider(private val movies: List<ModelSlider>) :
    CardStackAdapter<AdapterSlider.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_slider, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(movies[position].getThumb())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.imgSlider)
    }

    inner class MovieViewHolder(itemView: View) : CardStackView.ViewHolder(itemView) {
        var imgSlider: ImageView = itemView.findViewById(R.id.imgSlider)
    }

    override val itemCount: Int
        get() = movies.size
}