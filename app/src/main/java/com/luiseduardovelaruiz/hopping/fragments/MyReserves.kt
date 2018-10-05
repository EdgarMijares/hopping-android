package com.luiseduardovelaruiz.hopping.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.luiseduardovelaruiz.hopping.R
import com.luiseduardovelaruiz.hopping.adapters.MyReservesAdapter
import com.luiseduardovelaruiz.hopping.logic.Reservation
import kotlinx.android.synthetic.main.fragment_my_reserves.*
import java.util.ArrayList

/**
 *
 */
class MyReserves : Fragment() {

    var reservations: ArrayList<Reservation> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_reserves, container, false)
    }//end onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveReservations()
    }//end onViewCreated

    private fun configureReciclerView(array: ArrayList<Reservation>){
        myReservesRV.layoutManager = LinearLayoutManager(activity!!.baseContext)
        myReservesRV.adapter = MyReservesAdapter(array)
        my_reserves_progress_bar.visibility = View.INVISIBLE
    }//end configureReciclerView

    private fun retrieveReservations(){
        val reference = FirebaseDatabase.getInstance().getReference()
        reference.child(facebookUserID).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                resetContainers()
                for(child in p0.children){
                    for (child2 in child.children){
                        val date = child2.child("date").value as String
                        val lugarId = child2.child("lugar_id").value as String
                        val nombre = child2.child("nombre").value as String
                        val status = child2.child("status").value as String
                        val numPersonas = child2.child("num_personas").value as String
                        val reservation = Reservation(date,lugarId,status,nombre,numPersonas)
                        if (status == "aceptada") {
                            reservations.add(reservation)
                        }
                    }
                }
                configureReciclerView(reservations)
            }//end onDataChange
        })

        reference.child(facebookUserID).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                resetContainers()
                for(child in p0.children){
                    for (child2 in child.children){
                        val date = child2.child("date").value as String
                        val lugarId = child2.child("lugar_id").value as String
                        val nombre = child2.child("nombre").value as String
                        val status = child2.child("status").value as String
                        val numPersonas = child2.child("num_personas").value as String
                        val reservation = Reservation(date,lugarId,status,nombre,numPersonas)
                        if (status == "aceptada") {
                            reservations.add(reservation)
                        }
                    }
                }
                configureReciclerView(reservations)
            }//end onDataChange
        })
    }//end retrieveReservations

    private fun resetContainers(){
        reservations.removeAll(reservations)
    }
}//end class