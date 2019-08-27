package com.ngoclong.googlemapviewwithmarker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


object BitmapUtils {
    fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(context.getResources(), iconId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }


    /**
     * Demonstrates converting a [Drawable] to a [BitmapDescriptor],
     * for use as a marker icon.
     */
    private fun vectorToBitmap(context: Context, @DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor? {
        val vectorDrawable =
            ResourcesCompat.getDrawable(context.getResources(), id, null) ?: return null
        with(vectorDrawable) {
            val bitmap = Bitmap.createBitmap(
                intrinsicWidth,
                intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            setBounds(0, 0, canvas.width, canvas.height)
            DrawableCompat.setTint(this, color)
            draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}