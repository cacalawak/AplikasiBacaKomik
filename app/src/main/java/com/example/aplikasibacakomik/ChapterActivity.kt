package com.example.aplikasibacakomik

import android.app.ProgressDialog
import android.icu.text.CaseMap
import android.media.SubtitleData
import android.os.Bundle
import android.renderscript.RenderScript.Priority
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import org.json.JSONArray
import org.json.JSONObject

public class ChapterActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTitle, tvSubTitle;
    ImageView imgChapter;
    String ChapterEndpoint, Title, Subtitle;
    ModelChapter modelChapter;
    ViewPager viewPager;
    Button btnNext, btnPrev;
    ProgressDialog progressDialog;
    AdapterImageChapter adapter;
    List<ModelChapter> modelChapters = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang Menampilkan Gambar");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnable(True);

        modelChapter = (ModelChapter) getIntent().getSerializableExtra("detailChapter");
        if (modelChapter != null) {

            ChapterEndpoint = modelChapter.getChapterEndpoint();
            CaseMap.Title = modelChapter.getChapterTitle();

            tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText(CaseMap.Title);
            tvTitle.setSelected(true);

            tvSubTitle = findViewById(R.Id.tvSubTitle);
            tvSubTitle.setText(CaseMap.Title);

            viewPager = findViewById(R.Id.viewPager);
            btnNext = findViewById(R.Id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int currentItem = viewPager . getCurrentItem ();
                    viewPager.setCurrentItem(currentItem + 1);
                }
            } );
            btnPrev = findViewById(R.Id.btnPrev);
            btnPrev.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int currentItem = viewPager . getCurrentItem ();
                    viewPager.setCurrentItem(currentItem - 1);
                }
            } );

            getChapterImager();
        }
    }

    private void getChapterImage() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndPoint.CHAPTERURL)
            .addPathParameter("chapter_endpoint", ChapterEndPoint)
            .setPriority(Priority.HIGH)
            .builld()
            .getAsJSONObject(new JSONObjectRequestListener() {

                public void onResponse(JSONObject response) {
                    try{
                        progressDialog.dismiss();
                        JSONArrayplayerArray = response.getJSOONArray("chapter_image");
                        for (int i = 0; i <playerArray.length(); i++) {
                            JSONObject temp = playerArray.getJSONObject(i);
                            ModelChapter dataApi = new ModelChapter();
                            dataApi.setChapterImage(temp.getString("Chapter_image_link"));
                            dataApi.setImageNumber(temp.getString("image_number"));

                            Subtitle = response.getString("title");
                            tvSubtitle.setText(Subtitle);
                            modelChapters.add(Subtitle);
                            modelChapters.add(dataApi);
                            setImage();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChapterActivity.this,"Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onError(ANError an Error) {
                    progressDialog.dismiss();
                    Toast.makeText(ChapterActivity.this, "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();

                }
            });
    }

    private void setImage() {
        adapter = new AdapterImageChapter(modelChapters, this);
        viewPager.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}