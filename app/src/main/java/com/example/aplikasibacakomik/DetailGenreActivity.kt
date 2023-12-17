package com.example.aplikasibacakomik

import adapter.AdapterChapter
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
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.aplikasibacakomik.model.ModelChapter
import com.example.aplikasibacakomik.model.ModelKomik
import com.example.aplikasibacakomik.networking.ApiEndpoint
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.aplikasibacakomik.ChapterActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.List

class DetailGenreActivity : AppCompatActivity(), AdapterChapter.OnSelectData {
    private var toolbar: Toolbar? = null
    private var tvTitle: TextView? = null
    private var tvName: TextView? = null
    private var tvType: TextView? = null
    private var tvStatus: TextView? = null
    private var tvNameAuthor: TextView? = null
    private var tvTC: TextView? = null
    private var tvSynopsis: TextView? = null
    private var imgCover: ImageView? = null
    private var imgPhoto: ImageView? = null
    private var Endpoint: String? = null
    private var Cover: String? = null
    private var Thumb: String? = null
    private var Title: String? = null
    private var Type: String? = null
    private var Status: String? = null
    private var NameAuthor: String? = null
    private var Synopsis: String? = null
    private var modelKomik: ModelKomik? = null
    private var rvChapter: RecyclerView? = null
    private var adapterChapter: AdapterChapter? = null
    private var progressDialog: ProgressDialog? = null
    private var modelChapter: MutableList<ModelChapter> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupTranslucentStatus()

        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Mohon Tunggu")
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage("Sedang menampilkan detail")

        initViews()

        modelKomik = intent.getSerializableExtra("detailGenre") as ModelKomik?
        if (modelKomik != null) {
            loadData()
        }
    }

    private fun setupTranslucentStatus() {
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
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgCover = findViewById(R.id.imgCover)
        imgPhoto = findViewById(R.id.imgPhoto)
        tvTitle = findViewById(R.id.tvTitle)
        tvName = findViewById(R.id.tvName)
        tvType = findViewById(R.id.tvType)
        tvStatus = findViewById(R.id.tvStatus)
        tvNameAuthor = findViewById(R.id.tvNameAuthor)
        tvTC = findViewById(R.id.tvTC)
        tvSynopsis = findViewById(R.id.tvSynopsis)

        rvChapter = findViewById(R.id.rvChapter)
        rvChapter?.setHasFixedSize(true)
        rvChapter?.layoutManager = LinearLayoutManager(this)
    }

    private fun loadData() {
        Endpoint = modelKomik?.endpoint
        Title = modelKomik?.title
        Cover = modelKomik?.thumb

        tvTitle?.text = Title
        tvName?.text = Title
        tvTitle?.isSelected = true
        tvName?.isSelected = true
        tvTC?.visibility = View.GONE

        Glide.with(this)
            .load(Cover)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgCover)

        totalChapter
    }

    private val totalChapter: Unit
        private get() {
            progressDialog?.show()
            AndroidNetworking.get(ApiEndpoint.DETAILMANGAURL)
                .addPathParameter("endpoint", Endpoint)
                .setPriority(RenderScript.Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog?.dismiss()
                            Type = response.getString("type")
                            Status = response.getString("status")
                            NameAuthor = response.getString("author")
                            Synopsis = response.getString("synopsis")
                            Thumb = response.getString("thumb")

                            Glide.with(applicationContext)
                                .load(Thumb)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgPhoto)

                            tvType?.text = "Type : $Type"
                            tvStatus?.text = "Status : $Status"
                            tvNameAuthor?.text = NameAuthor
                            tvSynopsis?.text = Synopsis

                            val jsonArray: JSONArray = response.getJSONArray("chapter")
                            for (x in 0 until jsonArray.length()) {
                                val jsonObject: JSONObject = jsonArray.getJSONObject(x)
                                val dataApi = ModelChapter(
                                    jsonObject.getString("chapter_title"),
                                    jsonObject.getString("chapter_endpoint")
                                )
                                modelChapter.add(dataApi)
                            }
                            showAllChapter()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            showToast("Gagal menampilkan data!")
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog?.dismiss()
                        showToast("Tidak ada jaringan internet!")
                    }
                })
        }

    private fun showAllChapter() {
        adapterChapter = AdapterChapter(this@DetailGenreActivity, modelChapter, this)
        rvChapter?.adapter = adapterChapter
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailGenreActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSelected(modelChapter: ModelChapter?) {
        val intent = Intent(this@DetailGenreActivity, ChapterActivity::class.java)
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
        fun setWindowFlag(activity: AppCompatActivity, bits: Int, on: Boolean) {
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
