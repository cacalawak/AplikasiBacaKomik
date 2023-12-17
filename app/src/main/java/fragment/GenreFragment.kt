package fragment

import adapter.AdapterGenre
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.RenderScript
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.aplikasibacakomik.ListGenreActivity
import com.azhar.komik.networking.ApiEndpoint
import com.azhar.komik.utils.LayoutMarginDecoration
import com.azhar.komik.utils.Tools
import com.example.aplikasibacakomik.R
import model.ModelGenre
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class GenreFragment : Fragment(), AdapterGenre.OnSelectData {
    private var rvGenre: RecyclerView? = null
    private var adapterGenre: AdapterGenre? = null
    private var progressDialog: ProgressDialog? = null
    private var gridMargin: LayoutMarginDecoration? = null
    private val modelGenre: MutableList<ModelGenre> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_genre, container, false)
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setTitle("Mohon Tunggu")
        progressDialog?.setCancelable(false)
        progressDialog?.setMessage("Sedang menampilkan data")
        rvGenre = rootView.findViewById(R.id.rvGenre)
        rvGenre?.setHasFixedSize(true)
        val mLayoutManager = GridLayoutManager(
            requireActivity(),
            3, RecyclerView.VERTICAL, false
        )
        rvGenre?.layoutManager = mLayoutManager
        gridMargin = LayoutMarginDecoration(3, Tools.dp2px(requireActivity(), 12))
        rvGenre?.addItemDecoration(gridMargin!!)
        genre
    }

    private val genre: Unit
        get() {
            progressDialog?.show()
            AndroidNetworking.get(ApiEndpoint.GENREURL)
                .setPriority(RenderScript.Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            progressDialog?.dismiss()
                            val playerArray: JSONArray = response.getJSONArray("list_genre")
                            for (i in 0 until playerArray.length()) {
                                val temp: JSONObject = playerArray.getJSONObject(i)
                                val dataApi = ModelGenre(
                                    temp.getString("title"),
                                    temp.getString("endpoint")
                                )
                                modelGenre.add(dataApi)
                                showGenre()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                requireActivity(),
                                "Gagal menampilkan data!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        progressDialog?.dismiss()
                        Toast.makeText(
                            requireActivity(),
                            "Tidak ada jaringan internet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    private fun showGenre() {
        adapterGenre = AdapterGenre(requireActivity(), modelGenre, this)
        rvGenre?.adapter = adapterGenre
    }

    override fun onSelected(modelGenre: ModelGenre?) {
        val intent = Intent(requireActivity(), ListGenreActivity::class.java)
        intent.putExtra("listGenre", modelGenre)
        startActivity(intent)
    }
}