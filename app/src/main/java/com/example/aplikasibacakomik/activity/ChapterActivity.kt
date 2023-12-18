package com.example.aplikasibacakomik.activity

import com.example.aplikasibacakomik.adapter.AdapterImageChapter
import android.os.Bundle
import android.renderscript.RenderScript
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.aplikasibacakomik.R
import com.example.aplikasibacakomik.model.ModelChapter
import com.example.aplikasibacakomik.networking.ApiEndPoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ChapterActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var tvTitle: TextView? = null
    private var tvSubTitle: TextView? = null
    private var viewPager: ViewPager? = null
    private var btnNext: Button? = null
    private var btnPrev: Button? = null
    private var adapter: AdapterImageChapter? = null
    private var modelChapter: ModelChapter? = null
    private var ChapterEndpoint: String? = null
    private var Title: String? = null
    private var Subtitle: String? = null
    private var modelChapters: MutableList<ModelChapter> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        modelChapter = intent.getSerializableExtra("detailChapter") as ModelChapter?
        if (modelChapter != null) {
            ChapterEndpoint = modelChapter?.chapterEndpoint
            Title = modelChapter?.chapterTitle

            tvTitle = findViewById(R.id.tvTitle)
            tvTitle?.text = Title
            tvTitle?.isSelected = true

            tvSubTitle = findViewById(R.id.tvSubTitle)
            tvSubTitle?.text = Title

            viewPager = findViewById(R.id.viewPager)

            btnNext = findViewById(R.id.btnNext)
            btnNext?.setOnClickListener {
                viewPager?.currentItem = (viewPager?.currentItem ?: 0) + 1
            }

            btnPrev = findViewById(R.id.btnPrev)
            btnPrev?.setOnClickListener {
                viewPager?.currentItem = (viewPager?.currentItem ?: 0) - 1
            }

            if (ChapterEndpoint != null) {
                chapterImage()
            } else {
                showToast("Invalid chapter data")
            }
        } else {
            showToast("Invalid chapter data")
        }
    }

    private fun chapterImage() {
        AndroidNet.get(ApiEndPoint.CHAPTERURL)
            .addPathParameter("chapter_endpoint", ChapterEndpoint)
            .setPriority(RenderScript.Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val playerArray: JSONArray = response.getJSONArray("chapter_image")
                        for (i in 0 until playerArray.length()) {
                            val temp: JSONObject = playerArray.getJSONObject(i)
                            val dataApi = ModelChapter(
                                temp.getString("chapter_image_link"),
                                temp.getString("image_number")
                            )
                            modelChapters.add(dataApi)
                        }
                        Subtitle = response.getString("title")
                        tvSubTitle?.text = Subtitle
                        setImage()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        showToast("Gagal menampilkan data!")
                    }
                }

                override fun onError(anError: ANError?) {
                    showToast("Tidak ada jaringan internet!")
                }
            })
    }

    private fun setImage() {
        adapter = AdapterImageChapter(modelChapters, this)
        viewPager?.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ChapterActivity, message, Toast.LENGTH_SHORT).show()
    }
}