package com.luiseduardovelaruiz.hopping.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.fragments.placesArrayG
import com.luiseduardovelaruiz.hopping.logic.Reservation
import kotlinx.android.synthetic.main.my_reserves_cell.view.*
import java.util.ArrayList

class MyReservesAdapter(val reservations: ArrayList<Reservation>): RecyclerView.Adapter<MyReservesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyReservesHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.my_reserves_cell, parent, false)
        return MyReservesHolder(cellForRow)
    }//end onCreateViewHolder

    override fun getItemCount(): Int {
        return reservations.size
    }//end getItemCount

    override fun onBindViewHolder(holder: MyReservesHolder?, position: Int) {
        holder?.view?.my_reserves_date_textView?.text = reservations[position].date
        for(element in placesArrayG){
            if (element.id_place == reservations[position].place_id) {
                holder?.view?.my_reserves_placename_textView?.text = element.placename
                    Glide.with(holder!!.view!!.context).load("https://hoppingapp.com/profile-images/"+element.profileimage).into(holder.view.my_reserves_profile_ImageView)
                return
            }
        }//end for

    }//end onBindViewHolder

}
class MyReservesHolder(val view: View): RecyclerView.ViewHolder(view) {
}//end class