package com.luiseduardovelaruiz.hopping.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.luiseduardovelaruiz.hopping.FullScreenGallery
import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.R.id.horizontal_gallery_cell_image
import kotlinx.android.synthetic.main.horizontal_gallery_cell.view.*
import org.jetbrains.anko.custom.onUiThread
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class HorizontalGalleryRecyclerViewAdapter(var imagesArray: ArrayList<String>): RecyclerView.Adapter<MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        var view = holder?.view
        var imageView = holder?.view?.horizontal_gallery_cell_image
        view?.onClick {
            var intent = Intent(view.context, FullScreenGallery::class.java)
            intent.putExtra(NUMBER_OF_IMAGES, imagesArray.size)
            intent.putExtra(CURRENT_POSITION, position)
            intent.putStringArrayListExtra(IMAGES_ARRAY, imagesArray)
            view.context.startActivity(intent)
        }//end onCLick
        val path = view?.context!!.resources.getString(R.string.api_images)
        Glide.with(view?.context!!).load(path+imagesArray[position]).into(imageView!!)
    }//end onBindViewHolder

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.horizontal_gallery_cell, parent, false)
        return MyViewHolder(cellForRow)
    }//end onCreateViewHolder

    override fun getItemCount(): Int {
        return imagesArray.size
    }//end getItemCount

    companion object {
        val NUMBER_OF_IMAGES: String = "number_of_images"
        val CURRENT_POSITION: String = "current_position"
        val IMAGES_ARRAY: String = "images_array"
    }

}//end HorizontalGalleryRecyclerViewAdapter

class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
}