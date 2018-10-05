package com.luiseduardovelaruiz.hopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.luiseduardovelaruiz.hopping.fragments.FullScreenImage
import com.luiseduardovelaruiz.hopping.adapters.HorizontalGalleryFullScreenPagerAdapter
import com.luiseduardovelaruiz.hopping.adapters.HorizontalGalleryRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_full_screen_gallery.*
import org.jetbrains.anko.toast

class FullScreenGallery : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_gallery)

        var adapter = HorizontalGalleryFullScreenPagerAdapter(supportFragmentManager)

        val numberOfImages = intent.getIntExtra(HorizontalGalleryRecyclerViewAdapter.NUMBER_OF_IMAGES, -1)
        val currentPosition = intent.getIntExtra(HorizontalGalleryRecyclerViewAdapter.CURRENT_POSITION, -1)

        if (numberOfImages != -1) {
            for (i in 0..numberOfImages-1){
                adapter.addFragments(FullScreenImage())
            }
        }

        full_screen_view_pager.adapter = adapter

        if (currentPosition != -1) {
            full_screen_view_pager.setCurrentItem(currentPosition,false)
            toast("showing "+currentPosition+" image")
        }

    }//end onCreate

}//end class