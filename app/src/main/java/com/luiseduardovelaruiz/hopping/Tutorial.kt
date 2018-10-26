package com.luiseduardovelaruiz.hopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.luiseduardovelaruiz.hopping.controllers.SlideAdapterTotorial
import kotlinx.android.synthetic.main.activity_tutorial.*

class Tutorial : AppCompatActivity() {
//
//    private lateinit var slideViewPager: ViewPager
//    private lateinit var dootsLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        slideViewPager.adapter = SlideAdapterTotorial(this)
    }
}
