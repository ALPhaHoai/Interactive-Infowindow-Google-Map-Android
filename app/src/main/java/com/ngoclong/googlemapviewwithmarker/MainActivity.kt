package com.ngoclong.googlemapviewwithmarker

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
        private val TAG = MainActivity::class.simpleName
        private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private val markerIconSize = 60
    }

    internal var infoWindow: ViewGroup? = null
    internal var tireBlock: ViewGroup? = null
    internal var carInfo: ViewGroup? = null
    internal var carName: TextView? = null
    internal var closeBtn: Button? = null
    internal var callBtn: Button? = null
    internal var marker: Marker? = null
    internal var map: GoogleMap? = null
    private var mMapView: MapView? = null
    internal var isShowCarInfo: Boolean = false
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
        setupCarInfoWindow()
        map?.setOnMarkerClickListener {
            toggleCarInfo()
            false
        }

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
    }

    private fun setupCarInfoWindow() {
        infoWindow = layoutInflater.inflate(R.layout.car_infowindow, null) as ViewGroup
        tireBlock = infoWindow?.findViewById(R.id.tireLayout)
        carInfo = infoWindow?.findViewById(R.id.car_info)
        closeBtn = infoWindow?.findViewById(R.id.btn_onecar_exit)
        callBtn = infoWindow?.findViewById(R.id.btn_onecar_call)
        carName = infoWindow?.findViewById(R.id.car_name)
        tireBlock?.setOnTouchListener { v, event ->
            Toast.makeText(this@MainActivity, "Tire block clicked", Toast.LENGTH_SHORT).show()
            false
        }

        closeBtn?.setOnTouchListener { v, event ->
            hideCarInfo()
            false
        }

        callBtn?.setOnTouchListener { v, event ->
            Toast.makeText(this@MainActivity, "Call btn clicked", Toast.LENGTH_SHORT).show()
            false
        }

        val params = carName?.layoutParams as LinearLayout.LayoutParams
        params.bottomMargin = markerIconSize
        carName?.layoutParams = params

        baseLayout.addView(infoWindow)
        infoWindow?.visibility = View.INVISIBLE
    }

    override fun onCameraMove() {
        isMarkerCenter = false
        locateInfoWindow()
    }

    private fun locateInfoWindow() {
        val markerLocation = marker?.position
        val markerX: Float
        val markerY: Float//position of marker

        val projection = map?.projection
        val screenPosition = projection?.toScreenLocation(markerLocation)
        markerX = screenPosition?.x?.toFloat() ?: 0f
        markerY = screenPosition?.y?.toFloat() ?: 0f

        infoWindow?.x = markerX - (infoWindow?.width?.toFloat()?.div(2f) ?: 0f)
        infoWindow?.y = markerY - (tireBlock?.height?.toFloat() ?: 0f) - (carName?.height?.toFloat()
            ?: 0f) - markerIconSize / 2
    }

    internal fun toggleCarInfo() {
        if (isShowCarInfo) {
            hideCarInfo()
        } else {
            showCarInfo()
        }
    }

    internal fun hideCarInfo() {
        isShowCarInfo = false
        infoWindow?.visibility = View.VISIBLE
        tireBlock?.visibility = View.INVISIBLE
        carInfo?.visibility = View.INVISIBLE
    }

    internal fun showCarInfo() {
        infoWindow?.visibility = View.VISIBLE
        isShowCarInfo = true
        tireBlock?.visibility = View.VISIBLE
        carInfo?.visibility = View.VISIBLE
    }

    override fun onMapLoaded() {
        locateInfoWindow()
        hideCarInfo()
    }
}


