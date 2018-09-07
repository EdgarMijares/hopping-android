package com.example.luiseduardovelaruiz.hopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.luiseduardovelaruiz.hopping.fragments.MainMenu
import com.example.luiseduardovelaruiz.hopping.fragments.MediaFeed
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    var pagerAdapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        pagerAdapter = PagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragmentsToAdapter(MainMenu(),"Main Menu Fragment")
        pagerAdapter!!.addFragmentsToAdapter(MediaFeed(),"Media Feed Fragment")

        myViewPager.adapter = pagerAdapter


    }//end onCreate
}//end class MenuActivity