package com.luiseduardovelaruiz.hopping.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luiseduardovelaruiz.hopping.FullScreenGallery
import com.luiseduardovelaruiz.hopping.R
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class HorizontalGalleryRecyclerViewAdapter: RecyclerView.Adapter<MyViewHolder>() {

    val size = 6

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        var view = holder?.view
        view?.onClick {
            var intent = Intent(view.context, FullScreenGallery::class.java)
            intent.putExtra(NUMBER_OF_IMAGES, size)
            intent.putExtra(CURRENT_POSITION, position)
            view.context.startActivity(intent)
            view.context.toast("clicked "+position)
        }
    }//end onBindViewHolder

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.horizontal_gallery_cell, parent, false)
        return MyViewHolder(cellForRow)
    }//end onCreateViewHolder

    override fun getItemCount(): Int {
        return size
    }//end getItemCount

    companion object {
        val NUMBER_OF_IMAGES: String = "number_of_images"
        val CURRENT_POSITION: String = "current_position"
    }

}//end HorizontalGalleryRecyclerViewAdapter

class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
}