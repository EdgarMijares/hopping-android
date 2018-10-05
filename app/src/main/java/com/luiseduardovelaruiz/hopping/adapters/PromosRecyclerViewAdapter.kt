package com.luiseduardovelaruiz.hopping.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luiseduardovelaruiz.hopping.R

class PromosRecyclerViewAdapter: RecyclerView.Adapter<PromosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PromosViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.promos_cell, parent, false)
        return PromosViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: PromosViewHolder?, position: Int) {
    }

}//end PromosRecyclerViewAdapter

class PromosViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}