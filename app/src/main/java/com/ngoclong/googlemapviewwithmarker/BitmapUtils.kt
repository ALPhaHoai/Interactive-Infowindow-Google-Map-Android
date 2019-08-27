package com.ngoclong.googlemapviewwithmarker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory



object BitmapUtils {
    fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(context.getResources(), iconId)
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }
}