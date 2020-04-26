package com.gringo97.myapplication.defaultcluster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
import com.gringo97.myapplication.R
import com.gringo97.myapplication.defaultcluster.model.MyItem
import com.gringo97.myapplication.defaultcluster.model.MyItemReader
import org.json.JSONException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mClusterManager: ClusterManager<MyItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    @Throws(JSONException::class)
    private fun readItems() {
        val items = MyItemReader().read(resources.openRawResource(R.raw.radar_search))
        for (i in 0..9) {
            val offset = i / 60.0
            for (item in items) {
                val position = item.position
                val lat = position.latitude + offset
                val lng = position.longitude + offset
                val offsetItem = MyItem(lat, lng)
                mClusterManager?.addItem(offsetItem)
            }
        }
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

        mClusterManager = ClusterManager(this, mMap)
        mMap.setOnCameraIdleListener(mClusterManager)
        try {
            readItems()
        } catch (e: JSONException) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show()
        }


    }


}


