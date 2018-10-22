package com.luiseduardovelaruiz.hopping

import android.content.Intent
import android.net.Uri.parse
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.media.MediaPlayer.OnPreparedListener
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult

var myFaceBookData: String = ""

class LogInActivity : AppCompatActivity() {

    private var callbackManager : CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_SplashTheme)
        setContentView(R.layout.activity_login)

        var menuIntent: Intent = Intent(this@LogInActivity, MenuActivity::class.java)

        // If the access token is available already assign it.
        var accessToken = AccessToken.getCurrentAccessToken()

        if (accessToken != null){
            Log.d("MY-TAG", "Log status "+accessToken.toString())
            startActivity(menuIntent)
            finish()
        } else {
            Log.d("MY-TAG","Access token is null")
        }

        var videoPath = parse("android.resource://"+packageName+"/"+ R.raw.background)

        videoBackgroundView.setVideoURI(videoPath)
        videoBackgroundView.start()

        videoBackgroundView.setOnPreparedListener(OnPreparedListener { mp -> mp.isLooping = true })

        callbackManager = CallbackManager.Factory.create()

        // Callback registration
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                accessToken = loginResult.accessToken

                val request = GraphRequest.newMeRequest(
                        loginResult.accessToken
                ) { `object`, response ->
                    myFaceBookData = response.toString()
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,link,picture.type(large)")
                request.parameters = parameters
                request.executeAsync()
                startActivity(menuIntent)
                finish()
            }

            override fun onCancel() {
                Log.d("MY-TAG","cancelled")
                Toast.makeText(this@LogInActivity, "Canceled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Log.d("ERROR","we had an error" + exception.localizedMessage )
                Toast.makeText(this@LogInActivity, "onError", Toast.LENGTH_LONG).show()
            }
        })
    }//end onCreate

    override fun onActivityResult(requestCode: Int, responseCode: Int, intent: Intent) {
        callbackManager!!.onActivityResult(requestCode, responseCode, intent)
    }
}//end class LogInActivity