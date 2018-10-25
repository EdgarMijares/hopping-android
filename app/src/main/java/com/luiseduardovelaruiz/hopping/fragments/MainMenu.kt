package com.luiseduardovelaruiz.hopping.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.luiseduardovelaruiz.hopping.PlaceProfileActivity

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.controllers.Place
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_main_menu.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onUiThread
import org.jetbrains.anko.support.v4.toast
import java.util.*

var placesArrayG: ArrayList<Place> = ArrayList()

class MainMenu : Fragment() {

    lateinit var rootConstraintLayout: ConstraintLayout
    lateinit var placesArray: List<Place>

    lateinit var handler: Handler
    lateinit var runnable: Runnable

    val myReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val gson = GsonBuilder().create()
            val body = p1?.getStringExtra("dataBody")
            placesArray = gson.fromJson(body, Array<Place>::class.java).toList()
            placesArrayG = placesArray as ArrayList<Place>
            displayMenu(placesArray.size)
        }
    }//end myReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootConstraintLayout = fragment_main_menu_layout
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(myReceiver, IntentFilter("data_fetch_intent"))
        handler = Handler()
        main_swipe_refresh_layout.setOnRefreshListener {
            runnable = Runnable {
                main_swipe_refresh_layout.setRefreshing(true)
                onResume()
                main_swipe_refresh_layout.setRefreshing(false)
            }
            handler.postDelayed(runnable, 2000)
        }
    }//end onViewCreated

    fun displayMenu(placesCount: Int){

        val constraintSet = ConstraintSet()

        lateinit var previusRow: LinearLayout

        var indent = false
        var places = placesCount
        var rows = ArrayList<LinearLayout>()
        var percentageWidth = (rootConstraintLayout.width * 0.55).toInt()
        var params = LinearLayout.LayoutParams(percentageWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        var index = 0

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        if (places > 0){
            while (places != 0) {
                var row = LinearLayout(activity?.baseContext)
                if ((places - 2) >= 0) {
                    rows.add(row)
                    places -= 2
                } else {
                    rows.add(row)
                    places -= places
                }
            }//end while

        places = placesCount

        for (i in 1..rows.size){
            var row = rows[i-1]
            row.layoutParams = params
            row.id = i
            rootConstraintLayout.addView(row)
        }

        constraintSet.clone(rootConstraintLayout)

        for (row in rows) {

            if (rows.first() == row) {
                if (width == 540 && height == 960) {
                    constraintSet.connect(row.id, ConstraintSet.TOP, rootConstraintLayout.id, ConstraintSet.TOP,40)
                }
                if (width == 1080 && height == 1920) {
                    constraintSet.connect(row.id, ConstraintSet.TOP, rootConstraintLayout.id, ConstraintSet.TOP,80)
                }
            } else {
                constraintSet.connect(row.id, ConstraintSet.TOP, previusRow.id, ConstraintSet.BOTTOM,5)
            }

            constraintSet.connect(row.id, ConstraintSet.LEFT, rootConstraintLayout.id, ConstraintSet.LEFT)
            constraintSet.connect(row.id, ConstraintSet.RIGHT, rootConstraintLayout.id, ConstraintSet.RIGHT)

            if (indent) {
                constraintSet.setHorizontalBias(row.id, 0.60F)
                indent = false
            } else {
                constraintSet.setHorizontalBias(row.id, 0.40F)
                indent = true
            }

            /*
             *  Button insertion
             */
            val buttonParams = LinearLayout.LayoutParams(0, row.layoutParams.width / 2, 1.0F)
            buttonParams.leftMargin = 20

            if ((places - 2) >= 0) {
                for (i in 0..1) {
                    //val button = ImageView(activity?.baseContext)
                    val button = CircleImageView(activity?.baseContext)
                    val picURL = resources.getString(R.string.api_images)+placesArray[index].profileimage
                    val backgroudPictureURL = resources.getString(R.string.api_images)+placesArray[index].backgroundimage
                    val placeID = placesArray[index].id_place
                    val placeName = placesArray[index].placename
                    val placeDescription = placesArray[index].description
                    val latitude = placesArray[index].latitude
                    val longitude = placesArray[index].longitude

                    button.layoutParams = buttonParams
                    onUiThread {
                        Glide.with(activity!!.baseContext).load(picURL).into(button)
                    }
                    button.onClick {
                        val profileIntent: Intent = Intent(activity, PlaceProfileActivity::class.java)
                        profileIntent.putExtra(PROFILE_BACKGROUND_PICTURE_KEY, backgroudPictureURL)
                        profileIntent.putExtra(PROFILE_PICTURE_KEY, picURL)
                        profileIntent.putExtra(PID, placeID)
                        profileIntent.putExtra(PLACE_NAME, placeName)
                        profileIntent.putExtra(PLACE_DESCRIPTION, placeDescription)
                        profileIntent.putExtra(LATITUDE, latitude)
                        profileIntent.putExtra(LONGITUDE, longitude)
                        startActivity(profileIntent)
                    }
                    index++
                    row.addView(button)
                }//end for
                places -= 2
            } else {
                val picURL = resources.getString(R.string.api_images)+placesArray[index].profileimage
                val backgroudPictureURL = resources.getString(R.string.api_images)+placesArray[index].backgroundimage
                val placeID = placesArray[index].id_place
                val placeName = placesArray[index].placename
                val placeDescription = placesArray[index].description
                val latitude = placesArray[index].latitude
                val longitude = placesArray[index].longitude

                val button = CircleImageView(activity?.baseContext)

                button.layoutParams = buttonParams
                onUiThread {
                    Glide.with(activity!!.baseContext).load(picURL).into(button)
                }
                button.onClick {
                    val profileIntent: Intent = Intent(activity, PlaceProfileActivity::class.java)
                    profileIntent.putExtra(PROFILE_BACKGROUND_PICTURE_KEY, backgroudPictureURL)
                    profileIntent.putExtra(PROFILE_PICTURE_KEY, picURL)
                    profileIntent.putExtra(PID, placeID)
                    profileIntent.putExtra(PLACE_NAME, placeName)
                    profileIntent.putExtra(PLACE_DESCRIPTION, placeDescription)
                    profileIntent.putExtra(LATITUDE, latitude)
                    profileIntent.putExtra(LONGITUDE, longitude)
                    startActivity(profileIntent)
                }
                row.addView(button)

                val paddingButton = ImageView(activity?.baseContext)
                paddingButton.layoutParams = buttonParams
                paddingButton.backgroundColor = Color.BLACK
                paddingButton.isEnabled = false
                row.addView(paddingButton)
                places -= places
            }

            previusRow = row
        }//end for row in rows

        constraintSet.applyTo(rootConstraintLayout)

        }//end if places > 0

    }//end displayMenu

    companion object {
        val PROFILE_BACKGROUND_PICTURE_KEY = "place-background-image"
        val PROFILE_PICTURE_KEY = "place-image"
        val PID = "place-id"
        val PROMOS = "promos"
        val PLACE_NAME = "place-name"
        val PLACE_DESCRIPTION = "place_description"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"
    }

}//end MainMenu