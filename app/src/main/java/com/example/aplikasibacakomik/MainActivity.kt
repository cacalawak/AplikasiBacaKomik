package com.example.aplikasibacakomik.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.akshay.library.CurveBottomBar
import com.example.aplikasibacakomik.R
import fragment.HomeFragment
import com.example.aplikasibacakomik.utils.BottomBarBehavior
import fragment.GenreFragment


class MainActivity : AppCompatActivity() {
    private var navigation: CurveBottomBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation = findViewById(R.id.curveBottomBar)
        navigation?.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val layoutParams: CoordinatorLayout.LayoutParams =
            navigation?.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomBarBehavior()

        if (savedInstanceState == null) {
            navigation?.setSelectedItemId(R.id.navigation_home)
        }
    }

    private val mOnNavigationItemSelectedListener: CurveBottomBar.OnNavigationItemSelectedListener =
        object : CurveBottomBar.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val fragment: Fragment
                when (item.itemId) {
                    R.id.navigation_home -> {
                        fragment = HomeFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.frameContainer,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                    R.id.navigation_list -> {
                        fragment = GenreFragment()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.frameContainer,
                                fragment,
                                fragment::class.java.simpleName
                            )
                            .commit()
                        return true
                    }
                }
                return false
            }
        }
}
