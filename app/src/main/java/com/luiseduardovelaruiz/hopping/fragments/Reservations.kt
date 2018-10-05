package com.luiseduardovelaruiz.hopping.fragments

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.logic.DateManager
import kotlinx.android.synthetic.main.fragment_reservations.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

/**
 *
 *
 */
class Reservations : Fragment() {

    private var PID = ""
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reservations, container, false)
    }//end onCreateView

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservation_button.onClick {
            reservation_button.isEnabled = false
            var name: String = editText_reservation_name.text.toString()
            var numberOfPeople: String = editText_reservation_number_of_people.text.toString()
            sendReservationRequest(name, numberOfPeople)
        }
        PID = arguments.getString(MainMenu.PID)
        databaseReference = FirebaseDatabase.getInstance().getReference()

        progressBar2.visibility = View.VISIBLE
        checkForExistingDatePaths()
    }//end onViewCreated

    private fun sendReservationRequest(reservationName: String, numberOfPeople: String) {
        val values = HashMap<String, String>()
        values.put("date", DateManager().getTodaysDateWithDashFormat())
        values.put("lugar_id", PID)
        values.put("nombre", reservationName)
        values.put("num_personas", numberOfPeople)
        values.put("status", "pendiente")
        values.put("fcmToken", "0")
        databaseReference.child(facebookUserID).child(DateManager().getTodaysDateWithDashFormat()).child(UUID.randomUUID().toString()).setValue(values)
        activity.fragmentManager.popBackStack()
    }//end sendReservationRequest

    private fun checkForExistingReserves(){
        databaseReference.child(facebookUserID).child(DateManager().getTodaysDateWithDashFormat()).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (child in p0.children) {
                    val lugarId = child.child("lugar_id").value
                    if( lugarId == PID ){
                        progressBar2.visibility = View.INVISIBLE
                        longToast("Parece que ya reservaste en este lugar para hoy, espera a que el lugar te confirme o reserva en otro lugar")
                        activity.fragmentManager.popBackStack()
                    } else {
                        progressBar2.visibility = View.INVISIBLE
                        reservations_panel.visibility = View.VISIBLE
                    }
                }
            }
        })
    }//end checkForExistingReserves

    private fun checkForExistingDatePaths(){
        databaseReference.child(facebookUserID).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                println(p0)
                if(!p0.hasChild(DateManager().getTodaysDateWithDashFormat())){
                    progressBar2.visibility = View.INVISIBLE
                    reservations_panel.visibility = View.VISIBLE
                } else {
                    checkForExistingReserves()
                }
            }
        })
    }//end checkForExistingDatePaths
}//end Reservations