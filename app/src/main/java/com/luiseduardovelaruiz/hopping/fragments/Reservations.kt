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
import org.jetbrains.anko.toast
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

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

            if (name != "" && numberOfPeople != "") {
                sendReservationRequest(name, numberOfPeople)
            } else {
                reservation_button.isEnabled = true
                longToast("Escribe tu nombre y la cantidad de invitados para poder reservar")
            }
        }
        PID = arguments.getString(MainMenu.PID)
        databaseReference = FirebaseDatabase.getInstance().getReference()

        progressBar2.visibility = View.VISIBLE

        checkForMyHopConfigurations()
    }//end onViewCreated

    private fun checkForMyHopConfigurations(){
        val hopRef = FirebaseDatabase.getInstance(resources.getString(R.string.hop_conf)).getReference()
        hopRef.child(PID+"_hop").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_WEEK)
                var canReserve = false
                when(day) {
                    1 -> canReserve = p0.child("dom").value as Boolean
                    2 -> canReserve = p0.child("lun").value as Boolean
                    3 -> canReserve = p0.child("mar").value as Boolean
                    4 -> canReserve = p0.child("mie").value as Boolean
                    5 -> canReserve = p0.child("jue").value as Boolean
                    6 -> canReserve = p0.child("vie").value as Boolean
                    7 -> canReserve = p0.child("sab").value as Boolean
                }

                if (canReserve) {
                    val calendar = Calendar.getInstance()
                    var currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    val startHour = p0.child("startHour").value as Long
                    val endHour = p0.child("endHour").value as Long

                    if (startHour <= currentHour && currentHour <= endHour) {
                        checkForExistingDatePaths()
                    } else {
                        longToast("Este lugar no esta aceptando reservas en este momento, el horario de reservas es de "+startHour+" a "+endHour)
                        activity.fragmentManager.popBackStack()
                    }
                } else {
                    longToast("Este lugar no esta aceptando reservas el dia de hoy")
                    activity.fragmentManager.popBackStack()
                }
            }
        })
    }//end checkForMyHopConfigurations

    private fun sendReservationRequest(reservationName: String, numberOfPeople: String) {
        val values = HashMap<String, String>()
        values.put("date", DateManager().getTodaysDateWithDashFormat())
        values.put("lugar_id", PID)
        values.put("nombre", reservationName)
        values.put("num_personas", numberOfPeople)
        values.put("status", "pendiente")
        values.put("fcmToken", "0")
        values.put("tiempo","desconocido")
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