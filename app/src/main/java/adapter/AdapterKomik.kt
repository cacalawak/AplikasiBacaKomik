package adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibacakomik.R

class AdapterKomik(context: Context, items: List<ModelKomik>, xSelectData: onSelectData) :
    Adapter<AdapterKomik.ViewHolder?>() {
    private val items: List<ModelKomik>
    private val onSelectData: onSelectData
    private val mContext: Context

    interface onSelectData {
        fun onSelected(modelKomik: ModelKomik?)
    }

    init {
        mContext = context
        this.items = items
        onSelectData = xSelectData
    }

    @Override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_komik, parent, false)
        return ViewHolder(view)
    }

    @Override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ModelKomik = items[position]
        if (data.getType()
                .equals("Manhua")
        ) holder.tvType.setTextColor(Color.parseColor("#ff27AE60")) else if (data.getType()
                .equals("Manhwa")
        ) holder.tvType.setTextColor(Color.parseColor("#ffF2994A")) else if (data.getType()
                .equals("Manga")
        ) holder.tvType.setTextColor(Color.parseColor("#ffE8505B"))
        Glide.with(mContext)
            .load(data.getThumb())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.imgPhoto)
        holder.tvTitle.setText(data.getTitle())
        holder.tvType.setText(data.getType())
        holder.tvDate.setText(data.getUpdated())
        holder.cvTerbaru.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                onSelectData.onSelected(data)
            }
        })
    }

    @get:Override
    val itemCount: Int
        get() = items.size()

    //Class Holder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvType: TextView
        var tvDate: TextView
        var cvTerbaru: CardView
        var imgPhoto: ImageView

        init {
            cvTerbaru = itemView.findViewById(R.id.cvTerbaru)
            imgPhoto = itemView.findViewById(R.id.imgPhoto)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvType = itemView.findViewById(R.id.tvType)
            tvDate = itemView.findViewById(R.id.tvDate)
        }
    }
}