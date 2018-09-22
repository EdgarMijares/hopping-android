package com.luiseduardovelaruiz.hopping.fragments

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.logic.ReservationFBDB
import kotlinx.android.synthetic.main.fragment_reservations.*
import okhttp3.*
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.IOException
import java.net.URL

/**
 *
 *
 */
class Reservations : Fragment() {

    private var url = URL("https://www.hoppingapp.com/logic/reserve.php")
    private var PID = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reservations, container, false)
    }//end onCreateView

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservation_button.onClick {
            sendReservationRequest("Alexis N", "10")
        }
        PID = arguments.getString(MainMenu.PID)
        println("check fb id "+ facebookUserID)
    }//end onViewCreated

    private fun sendReservationRequest(reservationName: String, numberOfPeople: String) {

        val okHttpClient = OkHttpClient()
        val formBodyBuilder = FormBody.Builder()

        formBodyBuilder.add("name",reservationName)
        formBodyBuilder.add("numberOfPeople",numberOfPeople)
        formBodyBuilder.add("PID", PID)
        formBodyBuilder.add("fb_user_id", facebookUserID)

        var body = formBodyBuilder.build()
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        okHttpClient.newCall(request).enqueue(object: Callback{

            override fun onFailure(call: Call, e: IOException) {
                println("POST REQUEST FAILED")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)
                println("POST REQUEST SUCCESSFUL")

                val databaseReference = FirebaseDatabase.getInstance().getReference()
                val newReservationReference = databaseReference.child(facebookUserID)
                newReservationReference.child("name").setValue("alexis")
            }

        })
    }//end sendReservationRequest

}//end Reservations