package com.luiseduardovelaruiz.hopping.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.luiseduardovelaruiz.hopping.LogInActivity

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.myFaceBookData
import com.facebook.GraphRequest
import kotlinx.android.synthetic.main.fragment_side_menu.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

import com.facebook.login.LoginManager
import com.facebook.GraphResponse
import com.google.gson.GsonBuilder
import org.jetbrains.anko.support.v4.onUiThread
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 *
 */
class SideMenu : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_side_menu, container, false)
    }//end onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            myFaceBookData = response.toString()
            var jsonObject = response.jsonObject

            println("FACEBOOK DATA "+ myFaceBookData)

            // GET FACEBOOK'S USER PROFILE IMAGE
            if (jsonObject.has("picture")) {
                var profilePictureURL = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")
                onUiThread {
                    Glide.with(this).load(profilePictureURL).into(profilePictureImageView)
                }
            }

            //GET FACEBOOK'S USER UNIQUE ID
            if (jsonObject.has("id")) {
                var id = jsonObject.getString("id")
                println("FOUND ID : "+id)
                toast(id)
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, link, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()

        showPoliticsButton.onClick {
            val transaction = fragmentManager!!.beginTransaction()
            val politicsFragment = Politics()
            transaction.add(R.id.mainActivityLayout, politicsFragment)
            transaction.addToBackStack(null)
            sideMenuLayout.visibility = View.INVISIBLE
            transaction.commit()
        }

        logOutButton.onClick {
            LoginManager.getInstance().logOut()
            var logInIntent: Intent = Intent(activity!!.baseContext, LogInActivity::class.java)
            startActivity(logInIntent)
            activity!!.finish()
        }

    }//end onViewCreated

}//end SideMenu Fragment