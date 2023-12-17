package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibacakomik.R


class AdapterSlider(movies: List<ModelSlider>) :
    CardSliderAdapter<AdapterSlider.MovieViewHolder?>() {
    private val movies: List<ModelSlider>

    init {
        this.movies = movies
    }

    @Override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_slider, parent, false)
        return MovieViewHolder(view)
    }

    @Override
    fun bindVH(viewHolder: MovieViewHolder, position: Int) {
        Glide.with(viewHolder.itemView)
            .load(movies[position].getThumb())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(Target.SIZE_ORIGINAL)
            .into(viewHolder.imgSlider)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemView: View
        var imgSlider: ImageView

        init {
            imgSlider = itemView.findViewById(R.id.imgSlider)
            this.itemView = itemView
        }
    }

    @get:Override
    val itemCount: Int
        get() = movies.size()
}