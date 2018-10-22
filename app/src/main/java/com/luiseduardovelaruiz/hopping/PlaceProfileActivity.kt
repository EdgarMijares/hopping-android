package com.luiseduardovelaruiz.hopping

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luiseduardovelaruiz.hopping.adapters.HorizontalGalleryRecyclerViewAdapter
import com.luiseduardovelaruiz.hopping.adapters.PlacesFoundAdapter
import com.luiseduardovelaruiz.hopping.controllers.Place
import com.luiseduardovelaruiz.hopping.fragments.*
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_place_profile.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PlaceProfileActivity : AppCompatActivity() {

    private var PID = ""
    lateinit var placesFoundAdapter: PlacesFoundAdapter
    var imagesArray: ArrayList<String> = ArrayList()
    var promos: ArrayList<String> = ArrayList()
    var searchListShowing = false
    lateinit var latitude: String
    lateinit var longitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_profile)

        PID = intent.getStringExtra(MainMenu.PID)

        activity_place_profile_horizontal_gallery.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        for(place in placesArrayG){
            if (place.id_place == PID) {
                for (promo in place.promos) {
                    val pro = promo.value
                    promos.add(pro)
                }

                for (i in 0..9) {
                    var key = "img_"+i
                    imagesArray.add(place.gallery[key]!!)
                }
            }
        }

        activity_place_profile_horizontal_gallery.adapter = HorizontalGalleryRecyclerViewAdapter(imagesArray)
        activity_place_profile_horizontal_gallery.adapter.notifyDataSetChanged()

        var backgroudPictureURL = intent.getStringExtra(MainMenu.PROFILE_BACKGROUND_PICTURE_KEY)
        var picURL = intent.getStringExtra(MainMenu.PROFILE_PICTURE_KEY)
        latitude = intent.getStringExtra(MainMenu.LATITUDE)
        longitude = intent.getStringExtra(MainMenu.LONGITUDE)

        Glide.with(this).load(backgroudPictureURL).apply(RequestOptions.bitmapTransform(BlurTransformation(10,3))).into(profileBackgroudPictureImageView)
        Glide.with(this).load(picURL).into(profileImage)

    }//end onCreate

    override fun onStart() {
        super.onStart()

        places_found_recyclers_view.layoutManager = LinearLayoutManager(this)
        placesFoundAdapter = PlacesFoundAdapter(placesArrayG)
        places_found_recyclers_view.adapter = placesFoundAdapter

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var screenWidth = displayMetrics.widthPixels
        var dim = (screenWidth * 0.5).toInt()
        profileImage.layoutParams = ConstraintLayout.LayoutParams(dim, dim)
        infoLL.layoutParams = ConstraintLayout.LayoutParams(dim,dim)

        var constraintSet = ConstraintSet()
        constraintSet.clone(place_profile_constraintLayout)
        constraintSet.connect(profileImage.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(profileImage.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(profileImage.id, ConstraintSet.TOP, activity_place_profile_top_guideline.id, ConstraintSet.BOTTOM, 40)
        constraintSet.connect(activity_place_profile_horizontal_gallery.id, ConstraintSet.TOP, profileImage.id, ConstraintSet.BOTTOM, 60)

        constraintSet.connect(infoLL.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(infoLL.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(infoLL.id, ConstraintSet.TOP, activity_place_profile_top_guideline.id, ConstraintSet.BOTTOM, 40)

        constraintSet.applyTo(place_profile_constraintLayout)

        profile_text_name.text = intent.getStringExtra(MainMenu.PLACE_NAME)
        profile_text_description.text = intent.getStringExtra(MainMenu.PLACE_DESCRIPTION)

        profileImage.onClick {
            profileImage.visibility = View.INVISIBLE
            infoLL.visibility = View.VISIBLE
        }

        infoLL.onClick {
            profileImage.visibility = View.VISIBLE
            infoLL.visibility = View.INVISIBLE
        }

        place_profile_searchButton.onClick {
            searchListShowing = true
            placeProfile_hoppingLogoImageView.visibility = View.INVISIBLE
            placeProfile_search_text_view.visibility = View.VISIBLE
            places_found_recyclers_view.visibility = View.VISIBLE
            var myInputMethodManager = placeProfile_search_text_view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            myInputMethodManager.showSoftInput(placeProfile_search_text_view, InputMethodManager.SHOW_FORCED)
            placeProfile_search_text_view.requestFocus()
            placeProfile_search_text_view.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    placesFoundAdapter.placesArray = filterPlaces(placeProfile_search_text_view.text.toString())
                    placesFoundAdapter.notifyDataSetChanged()
                }

            })
        }//end onClick

        place_profile_back_button.onClick {
            if (searchListShowing) {
                placeProfile_hoppingLogoImageView.visibility = View.VISIBLE
                placeProfile_search_text_view.visibility = View.INVISIBLE
                places_found_recyclers_view.visibility = View.INVISIBLE
                searchListShowing = false
            } else {
                onBackPressed()
            }
        }//end onClick

        place_profile_promos_button.onClick {
            val bundle = Bundle()
            bundle.putStringArrayList(MainMenu.PROMOS, promos)
            val transaction = fragmentManager.beginTransaction()
            val fragment = Promos()
            fragment.arguments = bundle
            transaction.add(R.id.place_profile_constraintLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        place_profile_location_button.onClick {
            val bundle = Bundle()
            bundle.putString(MainMenu.LATITUDE, latitude)
            bundle.putString(MainMenu.LONGITUDE, longitude)
            bundle.putString(MainMenu.PLACE_NAME, intent.getStringExtra(MainMenu.PLACE_NAME))
            val transaction = fragmentManager.beginTransaction()
            val mapFragment = HoppingMapView()
            mapFragment.arguments = bundle
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

    override fun onBackPressed() {
        if (searchListShowing) {
            placeProfile_hoppingLogoImageView.visibility = View.VISIBLE
            placeProfile_search_text_view.visibility = View.INVISIBLE
            places_found_recyclers_view.visibility = View.INVISIBLE
            searchListShowing = false
        } else {
            super.onBackPressed()
        }
    }

    private fun filterPlaces(textToFind: String): ArrayList<Place>{
        val filteredArray = ArrayList<Place>()
        for(place in placesArrayG){
            if (place.placename.contains(textToFind,true)) {
                filteredArray.add(place)
            }
        }
        return filteredArray
    }
}//end PlaceProfileActivity