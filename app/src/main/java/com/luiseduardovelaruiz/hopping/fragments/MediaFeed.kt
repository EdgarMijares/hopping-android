package com.luiseduardovelaruiz.hopping.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.logic.DateManager
import com.luiseduardovelaruiz.hopping.logic.FeedImage
import com.luiseduardovelaruiz.hopping.adapters.MediaFeedRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_media_feed.*
import java.util.*

class MediaFeed : Fragment() {

    val myPressedReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val imageURL = p1?.getStringExtra(MediaFeedRecyclerViewAdapter.IMAGE_URL)

            val transaction = fragmentManager?.beginTransaction()
            val feedImageFullFragment = FeedImageFull()
            var bundle = Bundle()
            bundle.putString("imageurl",imageURL)
            feedImageFullFragment.arguments = bundle
            transaction?.add(R.id.mainActivityLayout, feedImageFullFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }//end myReceiver

    val myReleasedReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            this@MediaFeed.fragmentManager?.popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_media_feed, container, false)
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRealtimeDatabase()

        var feedSwipeRefreshLayout = media_feed_swipe_refresh_layout
        feedSwipeRefreshLayout.setOnRefreshListener {
            configureRealtimeDatabase()
            feedSwipeRefreshLayout.isRefreshing = false
        }


        var id = UUID.randomUUID().toString()+"UT"
        println("dates")
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(myPressedReceiver, IntentFilter("feed_image_pressed"))
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(myReleasedReceiver, IntentFilter("feed_image_released"))
    }//end onViewCreated

    private fun configureRecyclerView(feedArray: ArrayList<FeedImage>){
        feedRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        feedRecyclerView.adapter = MediaFeedRecyclerViewAdapter(feedArray)
    }//end configureRecyclerView

    private fun configureRealtimeDatabase(){
        var imagesArray: ArrayList<FeedImage> = ArrayList()
        val ref = FirebaseDatabase.getInstance("https://hopping-dc414-abaeb.firebaseio.com/").getReference()
        ref.child(DateManager().getTodaysDateWithDashFormat()+"-Feed").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }//end onCancelled

            override fun onDataChange(p0: DataSnapshot) {
                for (child in p0.children) {
                    val uploadTime = child.child("uploadTime").value
                    val url = child.child("url").value
                    val image = FeedImage(url as String, uploadTime as String)
                    imagesArray.add(image)
                }
                configureRecyclerView(imagesArray)
            }//end onDataChange
        })
    }//end configureRealtimeDatabase

}//end class