package com.luiseduardovelaruiz.hopping.fragments

import android.os.Bundle
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.adapters.PromosRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_promos.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class Promos : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        promos_constraint_layout.onClick {
            fragmentManager.popBackStackImmediate()
        }

        var promos = arguments.getStringArrayList(MainMenu.PROMOS)

        promos_recycler_view.layoutManager = LinearLayoutManager(activity.baseContext)
        promos_recycler_view.adapter = PromosRecyclerViewAdapter(promos)

    }

}//end class