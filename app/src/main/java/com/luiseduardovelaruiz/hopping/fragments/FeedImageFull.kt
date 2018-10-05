package com.luiseduardovelaruiz.hopping.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.fragment_feed_image_full.*

/**
 * A simple [Fragment] subclass.
 *
 */
class FeedImageFull : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_image_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.get("imageurl")
        Glide.with(activity!!.baseContext).load(url).into(feed_full_image_view)
    }
}
