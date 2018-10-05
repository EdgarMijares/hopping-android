package com.luiseduardovelaruiz.hopping.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.luiseduardovelaruiz.hopping.R

/**
 * A simple [Fragment] subclass.
 *
 */
class FullScreenImage : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_image, container, false)
    }


}//end FullScreenImage