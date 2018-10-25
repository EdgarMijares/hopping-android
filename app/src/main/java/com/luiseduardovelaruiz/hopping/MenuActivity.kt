package com.luiseduardovelaruiz.hopping

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.luiseduardovelaruiz.hopping.adapters.PlacesFoundAdapter
import com.luiseduardovelaruiz.hopping.controllers.Place
import com.luiseduardovelaruiz.hopping.fragments.*
import com.luiseduardovelaruiz.hopping.logic.PagerAdapter
import kotlinx.android.synthetic.main.activity_menu.*
import okhttp3.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MenuActivity : AppCompatActivity() {

    var pagerAdapter: PagerAdapter? = null
    var buttonState: Int = 0
    var menuShowing = false
    var searchListShowing = false
    val myFragmentManager = supportFragmentManager

    val buttonIconChangeReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {

            var state = p1?.getIntExtra(SideMenu.MENU_BUTTON_STATUS, -1)

            when (state) {
                1 -> {
                    buttonState = 1
                    menuButton.setBackgroundResource(R.drawable.back_button)
                    menuButton.onClick {
                        menuShowing = false
                        LocalBroadcastManager.getInstance(this@MenuActivity).sendBroadcast(Intent("back_action"))
                    }
                }
                0 -> {
                    buttonState = 0
                    menuButton.setBackgroundResource(R.drawable.menu_bars_button)
                    menuButton.onClick {
                        menuButtonAction()
                    }
                }
            }//end when

        }//end onReceive
    }//end buttonIconChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
            ) { `object`, response ->
            myFaceBookData = response.toString()
            var jsonObject = response.jsonObject

            if(jsonObject != null){
                //GET FACEBOOK'S USER UNIQUE ID
                if (jsonObject.has("id")) {
                    var id = jsonObject.getString("id")
                    facebookUserID = id
                }
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, name, link, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()

        pagerAdapter = PagerAdapter(myFragmentManager)
        pagerAdapter!!.addFragmentsToAdapter(MainMenu())
        pagerAdapter!!.addFragmentsToAdapter(MediaFeed())

        myViewPager.adapter = pagerAdapter

        menuPlacesFoundRV.layoutManager = LinearLayoutManager(this)
        var placesFoundAdapter = PlacesFoundAdapter(placesArrayG)
        menuPlacesFoundRV.adapter = placesFoundAdapter


        menuButton.onClick {
            menuButtonAction()
        }

        activityMenu_searchButton.onClick {
            searchListShowing = true
            mainMenu_hoppingLogoImageView.visibility = View.INVISIBLE
            edittext_menu.visibility = View.VISIBLE
            menuPlacesFoundRV.visibility = View.VISIBLE
            var myInputMethodManager = edittext_menu.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            myInputMethodManager.showSoftInput(edittext_menu, InputMethodManager.SHOW_FORCED)
            edittext_menu.requestFocus()
            edittext_menu.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    placesFoundAdapter.placesArray = filterPlaces(edittext_menu.text.toString())
                    placesFoundAdapter.notifyDataSetChanged()
                }

            })
        }//end onClick

//        activity_menu_camera_button.onClick {
//            // Open Built in Camera
//            /*
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivity(cameraIntent)
//            */
//
//            println("camera clicked")
//
//            /*
//             *  Android API Level checkout, camera2 API is only supported in Android 5 or newer
//             */
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//                    println("Camera 2 Intent will execute")
//
//                    val cameraIntent = Intent(this@MenuActivity, Camera::class.java)
//                    startActivity(cameraIntent)
//            } else {
//                //opcion para celulares con API level menor a 21 (android 5.0 Lollipop)
//            }
//
//        }//end onClick ...camera_button

        fetchData()

        LocalBroadcastManager.getInstance(this).registerReceiver(buttonIconChangeReceiver, IntentFilter("menu_button_icon_change"))

    }//end onCreate

    fun menuButtonAction(){
        if (!menuShowing) {
            menuShowing = true
            val transaction = myFragmentManager.beginTransaction()
            val sideMenuFragment = SideMenu()
            transaction.add(R.id.mainActivityLayout, sideMenuFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }//end menuButtonAction

    fun fetchData(){
        val url = resources.getString(R.string.api_url)
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to enque request..."+e.localizedMessage)
            }//end onFailure

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val intent = Intent("data_fetch_intent")
                intent.putExtra("dataBody",body)
                LocalBroadcastManager.getInstance(this@MenuActivity).sendBroadcast(intent)
            }//end onResponse
        })
    }//end fetchData

    override fun onBackPressed() {
        if (searchListShowing) {
            mainMenu_hoppingLogoImageView.visibility = View.VISIBLE
            edittext_menu.visibility = View.INVISIBLE
            menuPlacesFoundRV.visibility = View.INVISIBLE
            searchListShowing = false
        } else {
            super.onBackPressed()
            if (buttonState == 1) {
                buttonState = 0
                menuButton.setBackgroundResource(R.drawable.menu_bars_button)
            }

            if (menuShowing) {
                menuShowing = false
            }
        }
    }//end onBackPressed

    private fun filterPlaces(textToFind: String): ArrayList<Place>{
        val filteredArray = ArrayList<Place>()
        for(place in placesArrayG){
            if (place.placename.contains(textToFind,true)) {
                filteredArray.add(place)
            }
        }
        return filteredArray
    }
}//end class MenuActivity