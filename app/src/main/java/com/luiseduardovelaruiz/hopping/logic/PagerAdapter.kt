package com.luiseduardovelaruiz.hopping.logic

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    var myFragmentItems: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return myFragmentItems[position]
    }

    override fun getCount(): Int {
        return myFragmentItems.size
    }

    fun addFragmentsToAdapter(fragmentItem: Fragment) {
        myFragmentItems.add(fragmentItem)
    }

}//end class PagerAdapter