package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibacakomik.R
import model.ModelChapter

class AdapterChapter(context: Context, items: List<ModelChapter>, xSelectData: onSelectData) :
    Adapter<AdapterChapter.ViewHolder?>() {
    private val items: List<ModelChapter>
    private val onSelectData: onSelectData
    private val mContext: Context

    interface onSelectData {
        fun onSelected(modelChapter: ModelChapter?)
    }

    init {
        mContext = context
        this.items = items
        onSelectData = xSelectData
    }

    @Override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_chapter, parent, false)
        return ViewHolder(view)
    }

    @Override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ModelChapter = items[position]
        holder.btnChapter.setText(data.getChapterTitle())
        holder.btnChapter.setOnClickListener(object : OnClickListener() {
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
        var llChapter: LinearLayout
        var btnChapter: Button

        init {
            llChapter = itemView.findViewById(R.id.llChapter)
            btnChapter = itemView.findViewById(R.id.btnChapter)
        }
    }
}