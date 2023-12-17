package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.example.aplikasibacakomik.R
import model.ModelChapter

class AdapterImageChapter(private val items: List<ModelChapter>, private val context: Context) :
    PagerAdapter() {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = layoutInflater.inflate(R.layout.list_item_detail_chapter, container, false)
        val data: ModelChapter = items[position]
        val imageView: ImageView = view.findViewById(R.id.imgPhoto)
        val tvPagination: TextView = view.findViewById(R.id.tvPagination)
        tvPagination.text = "Hal. " + data.imageNumber
        Glide.with(context)
            .load(data.chapterImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(Target.SIZE_ORIGINAL)
            .into(imageView)
        container.addView(view, 0)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
