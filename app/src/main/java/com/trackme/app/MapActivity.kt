package com.trackme.app

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapd) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        Handler().postDelayed({
            //doSomethingHere()

            val sydney = LatLng((17.453791).toDouble(), 78.553726)

            mMap.addMarker(MarkerOptions().position(sydney).title("HOME")).setIcon(
                BitmapDescriptorFactory.fromResource(R.drawable.car))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18F))
            coordinatorLayout.visibility=View.VISIBLE
            progressBar.visibility=View.INVISIBLE
        }, 500)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.isTrafficEnabled=true
        mMap.isBuildingsEnabled=false
        mMap.uiSettings.isTiltGesturesEnabled=false
            mMap.isIndoorEnabled=true
        // Add a marker in Sydney and move the camera


    }
}