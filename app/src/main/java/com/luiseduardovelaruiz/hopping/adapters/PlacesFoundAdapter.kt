package com.luiseduardovelaruiz.hopping.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.luiseduardovelaruiz.hopping.PlaceProfileActivity
import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.controllers.Place
import com.luiseduardovelaruiz.hopping.fragments.MainMenu
import kotlinx.android.synthetic.main.places_found_cell.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PlacesFoundAdapter(var placesArray: ArrayList<Place>): RecyclerView.Adapter<PlacesFoundViewHolder>() {

    val path = "https://hoppingapp.com/profile-images/"

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlacesFoundViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.places_found_cell, parent, false)
        return PlacesFoundViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return placesArray.size
    }

    override fun onBindViewHolder(holder: PlacesFoundViewHolder?, position: Int) {
        Glide.with(holder!!.view!!.context).load(holder!!.view!!.resources.getString(R.string.api_images)+placesArray[position].profileimage).into(holder.view.places_found_cell_image)
        holder?.view?.name_place_found_textView.text = placesArray[position].placename
        holder?.view?.onClick {
            val picURL = holder!!.view!!.resources.getString(R.string.api_images)+placesArray[position].profileimage
            val backgroudPictureURL = path+placesArray[position].backgroundimage
            val placeID = placesArray[position].id_place
            val latitude = placesArray[position].latitude
            val longitude = placesArray[position].longitude
            val profileIntent: Intent = Intent(holder?.view?.context , PlaceProfileActivity::class.java)
            profileIntent.putExtra(MainMenu.PROFILE_BACKGROUND_PICTURE_KEY, backgroudPictureURL)
            profileIntent.putExtra(MainMenu.PROFILE_PICTURE_KEY, picURL)
            profileIntent.putExtra(MainMenu.PID, placeID)
            profileIntent.putExtra(MainMenu.LATITUDE, latitude)
            profileIntent.putExtra(MainMenu.LONGITUDE, longitude)
            holder?.view?.context.startActivity(profileIntent)
        }
    }
}

class PlacesFoundViewHolder(var view: View): RecyclerView.ViewHolder(view){
}