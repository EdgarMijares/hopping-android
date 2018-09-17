package com.luiseduardovelaruiz.hopping

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.gson.GsonBuilder
import com.luiseduardovelaruiz.hopping.fragments.MainMenu
import com.luiseduardovelaruiz.hopping.fragments.MediaFeed
import com.luiseduardovelaruiz.hopping.fragments.SideMenu
import com.luiseduardovelaruiz.hopping.logic.PagerAdapter
import com.luiseduardovelaruiz.hopping.logic.Place
import kotlinx.android.synthetic.main.activity_menu.*
import okhttp3.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.IOException

class MenuActivity : AppCompatActivity() {

    var pagerAdapter: PagerAdapter? = null

    val myFragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        pagerAdapter = PagerAdapter(myFragmentManager)
        pagerAdapter!!.addFragmentsToAdapter(MainMenu(),"Main Menu Fragment")
        pagerAdapter!!.addFragmentsToAdapter(MediaFeed(),"Media Feed Fragment")

        myViewPager.adapter = pagerAdapter

        menuButton.onClick {
            menuButtonAction()
        }

        searchButton.onClick {
            toast("search button clicked")
        }

        fetchData()
    }//end onCreate

    fun menuButtonAction(){
        val transaction = myFragmentManager.beginTransaction()
        val sideMenuFragment = SideMenu()
        transaction.add(R.id.mainActivityLayout,sideMenuFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }//end menuButtonAction

    fun fetchData(){
        val url = "https://www.hoppingapp.com/logic/app_connection.php"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to enque request..."+e.localizedMessage)
            }//end onFailure

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println("Successful request")

                val intent = Intent("data_fetch_intent")
                intent.putExtra("dataBody",body)
                LocalBroadcastManager.getInstance(this@MenuActivity).sendBroadcast(intent)
            }//end onResponse
        })

    }//end fetchData

}//end class MenuActivity