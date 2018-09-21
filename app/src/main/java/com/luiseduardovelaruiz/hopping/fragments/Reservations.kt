package com.luiseduardovelaruiz.hopping.fragments

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.fragment_reservations.*
import okhttp3.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.IOException
import java.net.URL

/**
 *
 *
 */
class Reservations : Fragment() {

    private var url = URL("https://www.hoppingapp.com/logic/reserve.php")
    private var PID = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reservations, container, false)
    }//end onCreateView

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservation_button.onClick {
            sendReservationRequest()
        }
        PID = arguments.getInt(MainMenu.PID)
    }//end onViewCreated

    private fun sendReservationRequest() {

        val okHttpClient = OkHttpClient()
        val formBodyBuilder = FormBody.Builder()

        formBodyBuilder.add("name","Eduardo")
        formBodyBuilder.add("numberOfPeople","5")
        formBodyBuilder.add("PID", PID.toString())

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
            }

        })
    }//end sendReservationRequest

}//end Reservations