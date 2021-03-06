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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.sdk25.coroutines.onClick
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult


var myFaceBookData: String = ""

class LogInActivity : AppCompatActivity() {

    private var callbackManager : CallbackManager? = null
    private var menuIntent: Intent? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_SplashTheme)
        setContentView(R.layout.activity_login)

        menuIntent = Intent(this@LogInActivity, MenuActivity::class.java)

        // CREAR USER INVITED TOKEN
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener(this@LogInActivity, OnSuccessListener { task ->
                    Log.w("TokenFirebase", task.token)
                })

        // If the access token is available already assign it.
        var accessToken = AccessToken.getCurrentAccessToken()

        if (accessToken != null){
            Log.d("LOGIN_TIPO", "Log status " + accessToken.toString())
            menuActivity()
        } else {
            Log.d("LOGIN_TIPO","Access token is null")
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
                val credential: AuthCredential = FacebookAuthProvider.getCredential(accessToken.token)
                if(user != null){
                    user.linkWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful) {
                                    Log.d("LINK_FIREBASE", "llinkWhitCredential:succes")
//                                    val userLinked: FirebaseUser  = task.getResult().user
                                }
                            }
                }
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
            if(auth == null) {
                auth.signInAnonymously().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Log.d("LOGIN_FIREBASE", "signInAnonymously:success")
                        menuActivity()
                    } else {
                        Log.w("LOGIN_FIREBASE", "signInAnonymously:failure", task.getException());
                        Toast.makeText(this@LogInActivity, "No se pudo acceder como invitado intente mas tarde.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                menuActivity()
            }
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
            Log.d("SESSION", "No se pudo asignar menuIntem")
        }
    }

}//end class LogInActivity

