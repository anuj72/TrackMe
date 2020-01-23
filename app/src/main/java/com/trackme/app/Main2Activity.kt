package com.trackme.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    val position = LatLng(41.015137, 28.979530)

    var markerOptions = MarkerOptions().position(position)

    lateinit var marker : Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            // Set the map ready callback to receive the GoogleMap object
            getMapAsync{
                com.google.android.gms.maps.MapsInitializer.initialize(applicationContext)
                setMapLocation(it)
            }
        }

    }

    private fun setMapLocation(map : GoogleMap) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
            mapType = GoogleMap.MAP_TYPE_NORMAL
            setOnMapClickListener {
                if(::marker.isInitialized){
                    marker.remove()
                }
                markerOptions.position(it)
                marker = addMarker(markerOptions)
                Toast.makeText(this@Main2Activity, "Clicked on ", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
