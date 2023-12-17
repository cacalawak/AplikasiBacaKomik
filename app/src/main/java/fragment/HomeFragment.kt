package fragment

import adapter.AdapterKomik
import adapter.AdapterSlider
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.azhar.komik.activities.DetailPopulerActivity
import com.azhar.komik.model.ModelKomik
import com.azhar.komik.model.ModelSlider
import com.azhar.komik.networking.ApiEndpoint
import com.example.aplikasibacakomik.R
import com.github.islamkhsh.CardSliderViewPager
import model.ModelKomik
import model.ModelSlider
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.Calendar
import java.util.List

class HomeFragment : Fragment(), AdapterKomik.onSelectData {
    private var rvTerbaru: RecyclerView? = null
    private var adapterSlider: AdapterSlider? = null
    private var adapterKomik: AdapterKomik? = null
    private var progressDialog: ProgressDialog? = null
    private var cardSliderViewPager: CardSliderViewPager? = null
    private var modelKomik: List<ModelKomik> = ArrayList()
    private val modelSlider: List<ModelSlider> = ArrayList()
    private var greetText: TextView? = null
    private var spPage: Spinner? = null
    private val numberPage = arrayOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
    )
    private var Page: String? = null

    @Override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @Override
    fun onViewCreated(@NonNull rootView: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        progressDialog = ProgressDialog(getActivity())
        progressDialog.setTitle("Mohon Tunggu")
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Sedang menampilkan data")
        greetText = rootView.findViewById(R.id.tvGreeting)
        cardSliderViewPager = rootView.findViewById(R.id.viewPager)
        spPage = rootView.findViewById(R.id.spPage)
        val adpWisata: ArrayAdapter<String> =
            ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, numberPage)
        adpWisata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPage.setAdapter(adpWisata)
        spPage.setOnItemSelectedListener(object : OnItemSelectedListener() {
            fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val itemDB: Object = parent.getItemAtPosition(pos)
                Page = itemDB.toString()
                komikTerbaru
            }

            fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        rvTerbaru = rootView.findViewById(R.id.rvTerbaru)
        rvTerbaru.setHasFixedSize(true)
        rvTerbaru.setLayoutManager(LinearLayoutManager(getActivity()))
        greeting
        imageSlider
        //getKomikTerbaru();
    }

    @get:SuppressLint("SetTextI18n")
    private val greeting: Unit
        private get() {
            val calendar: Calendar = Calendar.getInstance()
            val timeOfDay: Int = calendar.get(Calendar.HOUR_OF_DAY)
            if (timeOfDay >= 0 && timeOfDay < 12) {
                greetText.setText("Selamat Pagi Azhar")
            } else if (timeOfDay >= 12 && timeOfDay < 15) {
                greetText.setText("Selamat Siang Azhar")
            } else if (timeOfDay >= 15 && timeOfDay < 18) {
                greetText.setText("Selamat Sore Azhar")
            } else if (timeOfDay >= 18 && timeOfDay < 24) {
                greetText.setText("Selamat Malam Azhar")
            }
        }
    private val imageSlider: Unit
        private get() {
            progressDialog.show()
            AndroidNetworking.get(ApiEndpoint.ALSOURL)
                .setPriority(RenderScript.Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener() {
                    @Override
                    fun onResponse(response: JSONObject) {
                        try {
                            progressDialog.dismiss()
                            val playerArray: JSONArray = response.getJSONArray("manga_list")
                            for (i in 0..7) {
                                val temp: JSONObject = playerArray.getJSONObject(i)
                                val dataApi = ModelSlider()
                                dataApi.setThumb(temp.getString("thumb"))
                                modelSlider.add(dataApi)
                                adapterSlider = AdapterSlider(modelSlider)
                            }
                            cardSliderViewPager.setAdapter(AdapterSlider(modelSlider))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                getActivity(),
                                "Gagal menampilkan data!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    @Override
                    fun onError(anError: ANError?) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            getActivity(),
                            "Tidak ada jaringan internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    private val komikTerbaru: Unit
        private get() {
            progressDialog.show()
            AndroidNetworking.get(ApiEndpoint.BASEURL + Page)
                .setPriority(RenderScript.Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener() {
                    @Override
                    fun onResponse(response: JSONObject) {
                        try {
                            progressDialog.dismiss()
                            modelKomik = ArrayList()
                            val playerArray: JSONArray = response.getJSONArray("manga_list")
                            for (i in 0 until playerArray.length()) {
                                val temp: JSONObject = playerArray.getJSONObject(i)
                                val dataApi = ModelKomik()
                                dataApi.setTitle(temp.getString("title"))
                                dataApi.setThumb(temp.getString("thumb"))
                                dataApi.setType(temp.getString("type"))
                                dataApi.setUpdated(temp.getString("updated_on"))
                                dataApi.setEndpoint(temp.getString("endpoint"))
                                dataApi.setChapter(temp.getString("chapter"))
                                modelKomik.add(dataApi)
                                showKomikTerbaru()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                getActivity(),
                                "Gagal menampilkan data!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    @Override
                    fun onError(anError: ANError?) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            getActivity(),
                            "Tidak ada jaringan internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showKomikTerbaru() {
        adapterKomik = AdapterKomik(getActivity(), modelKomik, this)
        rvTerbaru.setAdapter(adapterKomik)
        adapterKomik.notifyDataSetChanged()
    }

    @Override
    fun onSelected(modelKomik: ModelKomik?) {
        val intent = Intent(getActivity(), DetailPopulerActivity::class.java)
        intent.putExtra("detailKomik", modelKomik)
        startActivity(intent)
    }
}