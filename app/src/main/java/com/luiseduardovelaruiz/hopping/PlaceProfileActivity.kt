package com.luiseduardovelaruiz.hopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luiseduardovelaruiz.hopping.fragments.MainMenu
import com.luiseduardovelaruiz.hopping.fragments.Promos
import com.luiseduardovelaruiz.hopping.logic.HorizontalGalleryRecyclerViewAdapter
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_place_profile.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PlaceProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_profile)

        var backgroudPictureURL = intent.getStringExtra(MainMenu.PROFILE_BACKGROUND_PICTURE_KEY)
        var picURL = intent.getStringExtra(MainMenu.PROFILE_PICTURE_KEY)
        Glide.with(this).load(backgroudPictureURL).apply(RequestOptions.bitmapTransform(BlurTransformation(10,3))).into(profileBackgroudPictureImageView)
        Glide.with(this).load(picURL).into(profileImage)

        activity_place_profile_horizontal_gallery.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        activity_place_profile_horizontal_gallery.adapter = HorizontalGalleryRecyclerViewAdapter()

        place_profile_promos_button.onClick {
            val transaction = fragmentManager.beginTransaction()
            val fragment = Promos()
        }
    }//end onCreate

    override fun onStart() {
        super.onStart()
        // get device dimensions
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        var screenWidth = displayMetrics.widthPixels
        var dim = (screenWidth * 0.5).toInt()
        profileImage.layoutParams = ConstraintLayout.LayoutParams(dim,dim)

        var constraintSet = ConstraintSet()
        constraintSet.clone(place_profile_constraintLayout)
        constraintSet.connect(profileImage.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(profileImage.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        constraintSet.connect(profileImage.id, ConstraintSet.TOP, activity_place_profile_top_guideline.id, ConstraintSet.BOTTOM, 40)

        constraintSet.connect(activity_place_profile_horizontal_gallery.id, ConstraintSet.TOP, profileImage.id, ConstraintSet.BOTTOM, 60)

        constraintSet.connect(activity_place_profile_buttons_container.id, ConstraintSet.TOP, activity_place_profile_horizontal_gallery.id, ConstraintSet.BOTTOM, 120)

        constraintSet.applyTo(place_profile_constraintLayout)
    }



}//end PlaceProfileActivity