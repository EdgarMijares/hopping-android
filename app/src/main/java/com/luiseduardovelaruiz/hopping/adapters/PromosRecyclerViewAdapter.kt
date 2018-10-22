package com.luiseduardovelaruiz.hopping.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.promos_cell.view.*

class PromosRecyclerViewAdapter(var promosArray: ArrayList<String>): RecyclerView.Adapter<PromosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PromosViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.promos_cell, parent, false)
        return PromosViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return promosArray.size
    }//end getItemCount

    override fun onBindViewHolder(holder: PromosViewHolder?, position: Int) {

        val view = holder?.view!!
        val correctString = promosArray[position].replace("\\n","\r\n")
        view.promo_textView.text = correctString
        when (position) {
            0 -> view.day_imageView.setImageResource(R.drawable.lun)
            1 -> view.day_imageView.setImageResource(R.drawable.mar)
            2 -> view.day_imageView.setImageResource(R.drawable.mie)
            3 -> view.day_imageView.setImageResource(R.drawable.jue)
            4 -> view.day_imageView.setImageResource(R.drawable.vie)
            5 -> view.day_imageView.setImageResource(R.drawable.sab)
            6 -> view.day_imageView.setImageResource(R.drawable.dom)
        }
    }//end onBindViewHolder

}//end PromosRecyclerViewAdapter

class PromosViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}