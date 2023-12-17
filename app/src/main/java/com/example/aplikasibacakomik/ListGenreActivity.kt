package com.example.aplikasibacakomik

import adapter.AdapterListGenre
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.example.aplikasibacakomik.DetailGenreActivity
import com.example.aplikasibacakomik.R
import adapter.AdapterListGenre
import com.example.aplikasibacakomik.model.ModelGenre
import com.example.aplikasibacakomik.model.ModelKomik
import com.example.aplikasibacakomik.networking.ApiEndpoint
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.List

class ListGenreActivity : AppCompatActivity(), AdapterListGenre.OnSelectData {
    private var rvListGenre: RecyclerView? = null
    private var progressDialog: ProgressDialog? = null
    private var adapterListGenre: AdapterListGenre? = null
    private var tvType: TextView? = null
    private var modelKomik: List<ModelKomik> = ArrayList()
    private var modelGenre: ModelGenre? = null
    private var toolbar: Toolbar? = null
    private var endpoint: String? = null
    private var type: String? = null
    private var spPage: Spinner? = null
    private val numberPage = arrayOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    )
    private var page: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_genre)
        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Mohon Tunggu")
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage("Sedang menampilkan data")
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        modelGenre = intent.getSerializableExtra("listGenre") as ModelGenre?
        if (modelGenre != null) {
            endpoint = modelGenre?.endpoint
            type = modelGenre?.title
            rvListGenre = findViewById(R.id.rvListGenre)
            rvListGenre?.setHasFixedSize(true)
            rvListGenre?.layoutManager = LinearLayoutManager(this)
            tvType = findViewById(R.id.tvType)
            tvType?.text = type
            spPage = findViewById(R.id.spPage)
            val adpWisata: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, numberPage)
            adpWisata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPage?.adapter = adpWisata
            spPage?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener() {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    val itemDB: Any = parent.getItemAtPosition(pos)
                    page = itemDB.toString()
                    genreList
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private val genreList: Unit
        get() {
            progressDialog?.show()
            AndroidNetworking.get(ApiEndpoint.GENREDETAIL + page)
                .addPathParameter("endpoint", endpoint)
                .setPriority(RenderScript.Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog?.dismiss()
                            modelKomik = ArrayList()
                            val playerArray: JSONArray = response.getJSONArray("manga_list")
                            for (i in 0 until playerArray.length()) {
                                val temp: JSONObject = playerArray.getJSONObject(i)
                                val dataApi = ModelKomik(
                                    temp.getString("title"),
                                    temp.getString("type"),
                                    temp.getString("thumb"),
                                    temp.getString("endpoint")
                                )
                                modelKomik.add(dataApi)
                                showGenre()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@ListGenreActivity, "Gagal menampilkan data!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog?.dismiss()
                        Toast.makeText(
                            this@ListGenreActivity, "Tidak ada jaringan internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showGenre() {
        adapterListGenre = AdapterListGenre(this@ListGenreActivity, modelKomik, this)
        rvListGenre?.adapter = adapterListGenre
        adapterListGenre?.notifyDataSetChanged()
    }

    override fun onSelected(modelKomik: ModelKomik?) {
        val intent = Intent(this@ListGenreActivity, DetailGenreActivity::class.java)
        intent.putExtra("detailGenre", modelKomik)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
