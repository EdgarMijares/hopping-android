package com.example.luiseduardovelaruiz.hopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = Intent(this@SplashActivity, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }//end onCreate

}//end SplashActivity