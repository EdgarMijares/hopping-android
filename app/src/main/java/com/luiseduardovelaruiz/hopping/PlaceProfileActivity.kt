package com.luiseduardovelaruiz.hopping

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luiseduardovelaruiz.hopping.fragments.HoppingMapView
import com.luiseduardovelaruiz.hopping.fragments.MainMenu
import com.luiseduardovelaruiz.hopping.fragments.Promos
import com.luiseduardovelaruiz.hopping.fragments.Reservations
import com.luiseduardovelaruiz.hopping.adapters.HorizontalGalleryRecyclerViewAdapter
import com.luiseduardovelaruiz.hopping.adapters.PlacesFoundAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_place_profile.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PlaceProfileActivity : AppCompatActivity() {

    private var PID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_profile)

        var backgroudPictureURL = intent.getStringExtra(MainMenu.PROFILE_BACKGROUND_PICTURE_KEY)
        var picURL = intent.getStringExtra(MainMenu.PROFILE_PICTURE_KEY)
        Glide.with(this).load(backgroudPictureURL).apply(RequestOptions.bitmapTransform(BlurTransformation(10,3))).into(profileBackgroudPictureImageView)
        Glide.with(this).load(picURL).into(profileImage)

        activity_place_profile_horizontal_gallery.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        activity_place_profile_horizontal_gallery.adapter = HorizontalGalleryRecyclerViewAdapter()

        PID = intent.getStringExtra(MainMenu.PID)


        /*
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            println("version mayor a 19")
        }
        */

    }//end onCreate

    override fun onStart() {
        super.onStart()

        places_found_recyclers_view.layoutManager = LinearLayoutManager(this)
        places_found_recyclers_view.adapter = PlacesFoundAdapter()

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var screenWidth = displayMetrics.widthPixels
        var dim = (screenWidth * 0.5).toInt()
        profileImage.layoutParams = ConstraintLayout.LayoutParams(dim, dim)

        var constraintSet = ConstraintSet()
        constraintSet.clone(place_profile_constraintLayout)
        constraintSet.connect(profileImage.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(profileImage.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(profileImage.id, ConstraintSet.TOP, activity_place_profile_top_guideline.id, ConstraintSet.BOTTOM, 40)
        constraintSet.connect(activity_place_profile_horizontal_gallery.id, ConstraintSet.TOP, profileImage.id, ConstraintSet.BOTTOM, 60)
        constraintSet.applyTo(place_profile_constraintLayout)

        place_profile_searchButton.onClick {
            placeProfile_hoppingLogoImageView.visibility = View.INVISIBLE
            placeProfile_search_text_view.visibility = View.VISIBLE
            places_found_recyclers_view.visibility = View.VISIBLE
            var myInputMethodManager = placeProfile_search_text_view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            myInputMethodManager.showSoftInput(placeProfile_search_text_view, InputMethodManager.SHOW_FORCED)
            placeProfile_search_text_view.requestFocus()
        }//end onClick

        place_profile_back_button.onClick {
            onBackPressed()
        }//end onClick

        place_profile_promos_button.onClick {
            val transaction = fragmentManager.beginTransaction()
            val fragment = Promos()
            transaction.add(R.id.place_profile_constraintLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        place_profile_location_button.onClick {
            val transaction = fragmentManager.beginTransaction()
            val mapFragment = HoppingMapView()
            transaction.add(R.id.place_profile_constraintLayout, mapFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        place_profile_reserv_button.onClick {
            val bundle = Bundle()
            bundle.putString(MainMenu.PID, PID)
            val transaction = fragmentManager.beginTransaction()
            val reserveFragment = Reservations()
            reserveFragment.arguments = bundle
            transaction.add(R.id.place_profile_constraintLayout, reserveFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }//end onStart

}//end PlaceProfileActivity