package com.luiseduardovelaruiz.hopping.logic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luiseduardovelaruiz.hopping.R

class HorizontalGalleryRecyclerViewAdapter: RecyclerView.Adapter<ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.horizontal_gallery_cell, parent, false)
        return ViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return 6
    }
}

class ViewHolder(view: View): RecyclerView .ViewHolder(view) {

}