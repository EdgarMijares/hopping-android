package com.luiseduardovelaruiz.hopping.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.activity_place_profile.view.*


class PlacesFoundAdapter: RecyclerView.Adapter<PlacesFoundViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlacesFoundViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.places_found_cell, parent, false)
        return PlacesFoundViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: PlacesFoundViewHolder?, position: Int) {
    }

}

class PlacesFoundViewHolder(var view: View): RecyclerView.ViewHolder(view){
}