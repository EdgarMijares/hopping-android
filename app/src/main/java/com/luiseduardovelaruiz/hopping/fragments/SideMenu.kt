package com.luiseduardovelaruiz.hopping.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
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
import com.luiseduardovelaruiz.hopping.MenuActivity
import kotlinx.android.synthetic.main.activity_menu.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.onUiThread
import org.jetbrains.anko.support.v4.startActivity

/**
 * A simple [Fragment] subclass.
 *
 */

var facebookUserID: String = ""

class SideMenu : Fragment() {

    val backReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var intent = Intent("menu_button_icon_change")
            intent.putExtra(MENU_BUTTON_STATUS, 0)
            var broadcastManager = LocalBroadcastManager.getInstance(activity!!.baseContext)
            broadcastManager.sendBroadcast(intent)
            fragmentManager?.popBackStack()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_side_menu, container, false)
    }//end onCreate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(backReceiver, IntentFilter("back_action"))

        val request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken()
        ) { `object`, response ->
            myFaceBookData = response.toString()
            var jsonObject = response.jsonObject

            if(jsonObject != null) {
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
                    facebookUserID = id
                }
            } else {
//                logInButton.visibility = View.VISIBLE
                logOutButton.setText("INICIAR SESION")
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, name, link, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()

        myReserves.onClick {
            // Notify the change of icon on the menu button (bars for back arrow)
            var intent = Intent("menu_button_icon_change")
            intent.putExtra(MENU_BUTTON_STATUS, 1)
            LocalBroadcastManager.getInstance(activity!!.baseContext).sendBroadcast(intent)

            val transaction = fragmentManager!!.beginTransaction()
            val politicsFragment = MyReserves()
            transaction.add(R.id.mainActivityLayout, politicsFragment)
            transaction.addToBackStack(null)
            sideMenuLayout.visibility = View.INVISIBLE
            transaction.commit()
        }

        showPoliticsButton.onClick {
            // Notify the change of icon on the menu button (bars for back arrow)
            var intent = Intent("menu_button_icon_change")
            intent.putExtra(MENU_BUTTON_STATUS, 1)
            LocalBroadcastManager.getInstance(activity!!.baseContext).sendBroadcast(intent)

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
//            activity!!.finish()
        }


    }//end onViewCreated

    companion object {
        val MENU_BUTTON_STATUS = "menu_button_status"
    }

}//end SideMenu Fragment