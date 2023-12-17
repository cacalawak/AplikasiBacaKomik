package adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibacakomik.R


class AdapterGenre(context: Context, items: List<ModelGenre>, xSelectData: onSelectData) :
    Adapter<AdapterGenre.ViewHolder?>() {
    private val items: List<ModelGenre>
    private val onSelectData: onSelectData
    private val mContext: Context

    interface onSelectData {
        fun onSelected(modelGenre: ModelGenre?)
    }

    init {
        mContext = context
        this.items = items
        onSelectData = xSelectData
    }

    @Override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_genre, parent, false)
        return ViewHolder(view)
    }

    @Override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ModelGenre = items[position]
        holder.tvGenre.setText(data.getTitle())
        holder.llGenre.setOnClickListener(object : OnClickListener() {
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
        var llGenre: LinearLayout
        var tvGenre: TextView

        init {
            llGenre = itemView.findViewById(R.id.llGenre)
            tvGenre = itemView.findViewById(R.id.tvGenre)
        }
    }
}