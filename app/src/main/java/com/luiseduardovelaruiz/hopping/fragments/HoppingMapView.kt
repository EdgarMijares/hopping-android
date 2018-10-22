package com.luiseduardovelaruiz.hopping.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.luiseduardovelaruiz.hopping.R
import kotlinx.android.synthetic.main.fragment_map.*

class HoppingMapView : Fragment() {

    lateinit var myGoogleMap: GoogleMap
    lateinit var myMapView: MapView
    lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.fragment_map, container, false)
        return myView
    }//end onCreateView

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dLat = arguments.getString(MainMenu.LATITUDE).toDouble()
        var dLon = arguments.getString(MainMenu.LONGITUDE).toDouble()

        myMapView = map_fragment_mapView
        if (myMapView != null) {
            myMapView.onCreate(null)
            myMapView.onResume()
            myMapView.getMapAsync(OnMapReadyCallback {
                MapsInitializer.initialize(activity.baseContext)
                myGoogleMap = it
                it.mapType = GoogleMap.MAP_TYPE_NORMAL
                it.addMarker(MarkerOptions().position(LatLng(dLat,dLon)).title(arguments.getString(MainMenu.PLACE_NAME)))
                var cam = CameraPosition.builder().target(LatLng(dLat,dLon)).zoom(16F).bearing(0F).tilt(45F).build()
                it.moveCamera(CameraUpdateFactory.newCameraPosition(cam))
            })
        }
    }//end onViewCreated

}//end MapView