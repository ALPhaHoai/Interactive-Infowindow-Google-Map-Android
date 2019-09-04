package com.ngoclong.googlemapviewwithmarker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(
    val map: GoogleMap?,
    val marker: Marker?,
    val markerIconSize: Int,
    val context: Context,
    val baseLayout: ViewGroup
) {
    internal var infoWindow: ViewGroup? = null
    internal var tireBlock: ViewGroup? = null
    internal var carInfo: ViewGroup? = null
    internal var carName: TextView? = null
    internal var closeBtn: Button? = null
    internal var callBtn: Button? = null
    internal var isShowCarInfo: Boolean = false

    internal var animationEnable = true

    internal var onCloseBtnClickListener: View.OnTouchListener? = null
        set(value) {
            closeBtn?.setOnTouchListener(value)
        }
    internal var onCallBtnClickListener: View.OnTouchListener? = null
        set(value) {
            callBtn?.setOnTouchListener(value)
        }

    val DURATION_WINDOW_ANIMATION = 200

    init {
        setupCarInfoWindow()
    }

    private fun setupCarInfoWindow() {
        infoWindow =
            LayoutInflater.from(context).inflate(R.layout.car_infowindow, null) as ViewGroup
        tireBlock = infoWindow?.findViewById(R.id.tireLayout)
        carInfo = infoWindow?.findViewById(R.id.car_info)
        closeBtn = infoWindow?.findViewById(R.id.btn_onecar_exit)
        callBtn = infoWindow?.findViewById(R.id.btn_onecar_call)
        carName = infoWindow?.findViewById(R.id.car_name)
        tireBlock?.setOnTouchListener { v, event ->
            Toast.makeText(context, "Tire block clicked", Toast.LENGTH_SHORT).show()
            false
        }


        onCloseBtnClickListener = View.OnTouchListener { v, e -> hideCarInfoWithAnimation(); false }

        val params = carName?.layoutParams as LinearLayout.LayoutParams
        params.bottomMargin = markerIconSize
        carName?.layoutParams = params

        baseLayout.addView(infoWindow)
        infoWindow?.visibility = View.INVISIBLE
    }

    private fun moveTo(position: Pair<Float, Float>?) {
        val (x, y) = position ?: return
        infoWindow?.x = x - (infoWindow?.width?.toFloat()?.div(2f) ?: 0f)
        infoWindow?.y =
            y - (tireBlock?.height?.toFloat() ?: 0f) - (carName?.height?.toFloat()
                ?: 0f) - markerIconSize
    }

    fun moveToMarkerPosition() {
        moveTo(getMarkerPosition())
    }

    internal fun toggleCarInfo() {
        if (isShowCarInfo) {
            hideCarInfoWithAnimation()
        } else {
            showCarInfoWithAnimation()
        }
    }

    internal fun hideCarInfoWithAnimation() {
        infoWindow?.let {
            if (animationEnable) {
                val markerPosition = getMarkerPosition()
                if (markerPosition == null) {
                    hideCarInfo()
                } else {
                    val (centerContainerX, centerContainerY) = markerPosition
                    val hiddingAnimation = ScaleAnimation(
                        1f, 0f,
                        1f, 0f,
                        centerContainerX, centerContainerY
                    )

                    hiddingAnimation.setDuration(DURATION_WINDOW_ANIMATION.toLong())
                    hiddingAnimation.setInterpolator(DecelerateInterpolator())
                    hiddingAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationStart(animation: Animation) {
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            hideCarInfo()
                        }
                    })

                    it.startAnimation(hiddingAnimation)
                }
            } else {
                hideCarInfo()
            }
        }
    }

    internal fun showCarInfoWithAnimation() {
        infoWindow?.let {
            if (animationEnable) {
                val markerPosition = getMarkerPosition()
                if (markerPosition == null) {
                    showCarInfo()
                } else {
                    val (centerContainerX, centerContainerY) = markerPosition
                    val showingAnimation = ScaleAnimation(
                        0f, 1f,
                        0f, 1f,
                        centerContainerX, centerContainerY
                    )

                    showingAnimation.setDuration(DURATION_WINDOW_ANIMATION.toLong())
                    showingAnimation.setInterpolator(DecelerateInterpolator())
                    showingAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                        override fun onAnimationStart(animation: Animation) {
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            showCarInfo()
                        }
                    })

                    it.startAnimation(showingAnimation)
                }
            } else {
                showCarInfo()
            }
        }
    }

    internal fun hideCarInfo() {
        infoWindow ?: return
        isShowCarInfo = false
        infoWindow?.visibility = View.VISIBLE
        tireBlock?.visibility = View.INVISIBLE
        carInfo?.visibility = View.INVISIBLE
    }

    internal fun showCarInfo() {
        infoWindow ?: return
        infoWindow?.visibility = View.VISIBLE
        isShowCarInfo = true
        tireBlock?.visibility = View.VISIBLE
        carInfo?.visibility = View.VISIBLE
    }

    private fun getMarkerPosition(): Pair<Float, Float>? {
        val markerLocation = marker?.position ?: return null
        val projection = map?.projection ?: return null
        val screenPosition = projection.toScreenLocation(markerLocation)
        val markerX = screenPosition?.x?.toFloat() ?: 0f
        val markerY = screenPosition?.y?.toFloat() ?: 0f
        return Pair(markerX, markerY)
    }
}