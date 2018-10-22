package com.luiseduardovelaruiz.hopping.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.luiseduardovelaruiz.hopping.FullScreenGallery

import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.fragment_full_screen_image.*
import kotlinx.android.synthetic.main.fragment_full_screen_image.view.*
import org.jetbrains.anko.support.v4.toast

/**
 * A simple [Fragment] subclass.
 *
 */
class FullScreenImage : Fragment() {

    lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_full_screen_image, container, false)
        return myView
    }

    override fun onStart() {
        super.onStart()
        var url = arguments!!.getString(FullScreenGallery.IMAGE_URL)
        Glide.with(view?.context!!).load(resources.getString(R.string.api_images)+url).into(myView.imageView_fullscreeniamge)
    }

}//end FullScreenImage