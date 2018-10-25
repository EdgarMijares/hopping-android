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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.sdk25.coroutines.onClick
import com.google.android.gms.tasks.OnCompleteListener


var myFaceBookData: String = ""

class LogInActivity : AppCompatActivity() {

    private var callbackManager : CallbackManager? = null
    private var menuIntent: Intent? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_SplashTheme)
        setContentView(R.layout.activity_login)

        menuIntent = Intent(this@LogInActivity, MenuActivity::class.java)

        // CREAR USER INVITED TOKEN
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        user.getIdToken(true)
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        val idToken = task.getResult().getToken()
                        Log.d("ID TOKEN", idToken)
                    } else {
                        Log.e("ID TOKEN", "Error en validacion de token")
                    }
                })

        // If the access token is available already assign it.
        var accessToken = AccessToken.getCurrentAccessToken()

        if (accessToken != null){
            Log.d("MY-TAG", "Log status " + accessToken.toString())
            menuActivity()
        } else {
            Log.d("MY-TAG","Access token is null")
        }

        var videoPath = parse("android.resource://" + packageName + "/" + R.raw.background)

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

        guest_user_button.onClick {
            menuActivity()
        }
    }//end onCreate

    override fun onActivityResult(requestCode: Int, responseCode: Int, intent: Intent) {
        callbackManager!!.onActivityResult(requestCode, responseCode, intent)
    }

    fun menuActivity() {
        if(menuIntent != null){
            startActivity(menuIntent)
            finish()
        } else {
            Log.d("SESSION", "No se pudo asignar menuInten")
        }
    }

}//end class LogInActivity

