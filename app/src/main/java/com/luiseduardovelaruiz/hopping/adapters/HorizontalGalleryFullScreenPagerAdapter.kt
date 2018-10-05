package com.luiseduardovelaruiz.hopping.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class HorizontalGalleryFullScreenPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var myFragmentItems: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return myFragmentItems[position]
    }

    override fun getCount(): Int {
        return myFragmentItems.size
    }

    fun addFragments(fragmentItem: Fragment){
        myFragmentItems.add(fragmentItem)
    }

}//end HorizontalGalleryFullScreenPagerAdapter