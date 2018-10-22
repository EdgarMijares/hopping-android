package com.luiseduardovelaruiz.hopping.adapters

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.logic.FeedImage
import kotlinx.android.synthetic.main.feed_image_cell.view.*
import org.jetbrains.anko.windowManager
import java.util.*

class MediaFeedRecyclerViewAdapter(var imagesArray: ArrayList<FeedImage>): RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeedViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.feed_image_cell, parent, false)
        return FeedViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return imagesArray.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder?, position: Int) {
        var view = holder?.view
        var imageView = holder?.view?.feed_image

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        view?.context?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        /*
        if (width == 540 && height == 960) {
            imageView?.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,Random().nextInt((400 + 1) - 300) +  300)
        }

        if (width == 1080 && height == 1920) {
            imageView?.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,Random().nextInt((800 + 1) - 500) +  500)
        }
        */
        imageView?.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 500)
        view?.placeNameTextView?.text = ""
        //var minutes = (System.currentTimeMillis() - imagesArray[position].uploadInterval) / 1000 / 60
        view?.uploadTimeTextView?.text = imagesArray[position].uploadTime

        Glide.with(view!!.context).load(imagesArray[position].imageURL).into(imageView!!)

        view?.setOnLongClickListener(object: View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                val intent = Intent("feed_image_pressed")
                intent.putExtra(IMAGE_URL, imagesArray[position].imageURL)
                LocalBroadcastManager.getInstance(p0?.context!!).sendBroadcast(intent)
                return false
            }
        })

        view?.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when(p1?.action){
                    MotionEvent.ACTION_UP -> {
                        val intent = Intent("feed_image_released")
                        LocalBroadcastManager.getInstance(p0?.context!!).sendBroadcast(intent)
                    }
                }
                return false
            }
        })
    }//end onBindViewHolder

    companion object {
        var IMAGE_URL = "onTouch"
    }//end companion object

}//end MediaFeedRecyclerViewAdapter

class FeedViewHolder(val view: View): RecyclerView.ViewHolder(view) {
}