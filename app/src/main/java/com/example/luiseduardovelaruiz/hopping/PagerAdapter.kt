package com.example.luiseduardovelaruiz.hopping

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var myFragmentManager = fm
    var myFragmentItems: ArrayList<Fragment> = ArrayList()
    var myFragmentTitles: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return myFragmentItems[position]
    }

    override fun getCount(): Int {
        return myFragmentItems.size
    }

    fun addFragmentsToAdapter(fragmentItem: Fragment, fragmentTitle:String) {
        myFragmentItems.add(fragmentItem)
        myFragmentTitles.add(fragmentTitle)
    }

}//end class PagerAdapter