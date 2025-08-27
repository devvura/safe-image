package com.devfares.halal_view.util

import android.graphics.drawable.AnimatedVectorDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

fun ImageView.setDrawableRes(@DrawableRes resId: Int?) {
    resId.let {
        val drawable = resId?.let { it1 -> AppCompatResources.getDrawable(context, it1) }
        this.setImageDrawable(drawable)
        when (drawable) {
            is AnimatedVectorDrawable -> drawable.start()
            is AnimatedVectorDrawableCompat -> drawable.start()
        }
    }
}