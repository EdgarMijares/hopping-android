package com.luiseduardovelaruiz.hopping.fragments


import android.os.Bundle
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.logic.PromosRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_promos.*
import org.jetbrains.anko.sdk25.coroutines.onClick

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
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

        promos_recycler_view.layoutManager = LinearLayoutManager(activity.baseContext)
        promos_recycler_view.adapter = PromosRecyclerViewAdapter()

    }

}
