package com.luiseduardovelaruiz.hopping.fragments


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback

import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.fragment_map.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HoppingMapView : Fragment() {

    lateinit var myGoogleMap: GoogleMap
    lateinit var myMapView: MapView
    lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_map, container, false)
        return myView
    }//end onCreateView

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myMapView = map_fragment_mapView
        if (myMapView != null) {
            myMapView.onCreate(null)
            myMapView.onResume()
            myMapView.getMapAsync(OnMapReadyCallback {
                MapsInitializer.initialize(activity.baseContext)
                myGoogleMap = it
                it.mapType = GoogleMap.MAP_TYPE_NORMAL


            })
        }

    }//end onViewCreated

}//end MapView