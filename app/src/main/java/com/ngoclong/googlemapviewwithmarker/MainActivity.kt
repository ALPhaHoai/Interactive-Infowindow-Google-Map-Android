package com.ngoclong.googlemapviewwithmarker

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
    GoogleMap.OnMapLoadedCallback {

    companion object {
        private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private val markerIconSize = 60
    }

    internal var marker: Marker? = null
    internal var map: GoogleMap? = null
    private var mMapView: MapView? = null
    internal var customInfoWindow: CustomInfoWindow? = null
    internal var isMarkerCenter = true
    internal var latLng = LatLng(21.028511, 105.804817)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapViewBundle: Bundle? = savedInstanceState?.getBundle(MAPVIEW_BUNDLE_KEY)
        mMapView = findViewById(R.id.map) as MapView
        mMapView?.onCreate(mapViewBundle)
        mMapView?.getMapAsync(this)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }

        mMapView?.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val markerIcon =
            BitmapUtils.resizeMapIcons(this, R.drawable.car_icon, markerIconSize, markerIconSize)
        marker = map?.addMarker(
            MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory.fromBitmap(markerIcon)
            )
        )
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(marker?.position, 17f))
        map?.setOnCameraMoveListener(this)
        map?.setOnMapLoadedCallback(this)


        customInfoWindow = CustomInfoWindow(map, marker, markerIconSize, this, baseLayout)
        customInfoWindow?.onCallBtnClickListener = View.OnTouchListener { v, e ->
            Toast.makeText(this, "Call btn clicked", Toast.LENGTH_LONG).show()
            false
        }
        customInfoWindow?.onTireBlockClickListener = View.OnTouchListener { v, e ->
            Toast.makeText(this, "Tire block clicked", Toast.LENGTH_LONG).show()
            false
        }

        map?.setOnMarkerClickListener {
            customInfoWindow?.toggleCarInfo()
            false
        }
    }

    override fun onCameraMove() {
        isMarkerCenter = false
        customInfoWindow?.moveToMarkerPosition()
    }

    override fun onMapLoaded() {
        customInfoWindow?.moveToMarkerPosition()
        customInfoWindow?.hideCarInfo()
    }
}


