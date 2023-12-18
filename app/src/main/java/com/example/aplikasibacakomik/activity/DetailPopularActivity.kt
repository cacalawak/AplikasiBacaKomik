package com.example.aplikasibacakomik.activity


import com.example.aplikasibacakomik.adapter.AdapterChapter
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.aplikasibacakomik.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasibacakomik.model.ModelChapter
import com.example.aplikasibacakomik.model.ModelKomik
import com.example.aplikasibacakomik.networking.ApiEndPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.List

class DetailPopularActivity : AppCompatActivity(), AdapterChapter.OnSelectData {
    private var toolbar: Toolbar? = null
    private var tvTitle: TextView? = null
    private var tvName: TextView? = null
    private var tvType: TextView? = null
    private var tvStatus: TextView? = null
    private var tvNameAuthor: TextView? = null
    private var tvAllChapter: TextView? = null
    private var tvSynopsis: TextView? = null
    private var imgCover: ImageView? = null
    private var imgPhoto: ImageView? = null
    private var endpoint: String? = null
    private var cover: String? = null
    private var thumb: String? = null
    private var title: String? = null
    private var type: String? = null
    private var status: String? = null
    private var nameAuthor: String? = null
    private var allChapter: String? = null
    private var synopsis: String? = null
    private var modelKomik: ModelKomik? = null
    private var rvChapter: RecyclerView? = null
    private var adapterChapter: AdapterChapter? = null
    private var progressDialog: ProgressDialog? = null
    private var modelChapter: List<ModelChapter> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (Build.VERSION.SDK_INT < 34) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }

        if (Build.VERSION.SDK_INT >= 34) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 34) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }

        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Mohon Tunggu")
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage("Sedang menampilkan detail")

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        modelKomik = intent.getSerializableExtra("detailKomik") as ModelKomik?
        if (modelKomik != null) {
            endpoint = modelKomik?.endpoint
            title = modelKomik?.title
            cover = modelKomik?.thumb
            allChapter = modelKomik?.chapter

            imgCover = findViewById(R.id.imgCover)
            imgPhoto = findViewById(R.id.imgPhoto)
            tvTitle = findViewById(R.id.tvTitle)
            tvName = findViewById(R.id.tvName)
            tvType = findViewById(R.id.tvType)
            tvStatus = findViewById(R.id.tvStatus)
            tvNameAuthor = findViewById(R.id.tvNameAuthor)
            tvAllChapter = findViewById(R.id.tvAllChapter)
            tvSynopsis = findViewById(R.id.tvSynopsis)

            tvTitle?.text = title
            tvName?.text = title
            tvTitle?.isSelected = true
            tvName?.isSelected = true
            tvAllChapter?.text = allChapter

            Glide.with(this)
                .load(cover)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCover)

            rvChapter = findViewById(R.id.rvChapter)
            rvChapter?.setHasFixedSize(true)
            rvChapter?.layoutManager = LinearLayoutManager(this)

            totalChapter
        }
    }

    private val totalChapter: Unit
        private get() {
            progressDialog?.show()
            AndroidNetworking.get(ApiEndPoint.DETAILMANGAURL)
                .addPathParameter("endpoint", endpoint)
                .setPriority(RenderScript.Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog?.dismiss()
                            type = response.getString("type")
                            status = response.getString("status")
                            nameAuthor = response.getString("author")
                            synopsis = response.getString("synopsis")
                            thumb = response.getString("thumb")

                            Glide.with(applicationContext)
                                .load(thumb)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgPhoto)

                            tvType?.text = "Type : $type"
                            tvStatus?.text = "Status : $status"
                            tvNameAuthor?.text = nameAuthor
                            tvSynopsis?.text = synopsis

                            val jsonArray: JSONArray = response.getJSONArray("chapter")
                            for (x in 0 until jsonArray.length()) {
                                val jsonObject: JSONObject = jsonArray.getJSONObject(x)
                                val dataApi = ModelChapter(
                                    jsonObject.getString("chapter_title"),
                                    jsonObject.getString("chapter_endpoint")
                                )
                                modelChapter.add(dataApi)
                                showAllChapter()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@DetailPopulerActivity,
                                "Gagal menampilkan data!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog?.dismiss()
                        Toast.makeText(
                            this@DetailPopulerActivity, "Tidak ada jaringan internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showAllChapter() {
        adapterChapter = AdapterChapter(this@DetailPopulerActivity, ModelChapter, this)
        rvChapter?.adapter = adapterChapter
    }

    override fun onSelected(modelChapter: ModelChapter?) {
        val intent = Intent(this@DetailPopulerActivity, ChapterActivity::class.java)
        intent.putExtra("detailChapter", modelChapter)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window: Window = activity.window
            val winParams: WindowManager.LayoutParams = window.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            window.attributes = winParams
        }
    }
}
