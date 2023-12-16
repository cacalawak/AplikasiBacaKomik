package com.example.aplikasibacakomik

import android.app.ProgressDialog
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

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


}