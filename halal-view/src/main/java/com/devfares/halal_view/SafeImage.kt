package com.devfares.halal_view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.toBitmap
import com.devfares.halal_view.util.setDrawableRes

fun ImageView.loadSafeImage(
    imageUrl: Any,
    blurRadiusPx: Int = 50,
    onLoading: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    crossFadeEnabled: Boolean = true,
    @DrawableRes placeholderRes: Int? = null,
    @DrawableRes errorDrawableRes: Int? = null
) {
    val context = this.context
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .target(
            onStart = {
                setDrawableRes(placeholderRes)
                onLoading?.invoke()
            },
            onError = {
                setDrawableRes(errorDrawableRes)
                onError?.invoke()
            },

            onSuccess = { result ->
                this.setImageBitmap(result.toBitmap())
                onSuccess?.invoke()
            }
        )
        .apply {

            if (crossFadeEnabled) crossfade(true)
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)

            transformations(
                BlurHaramTransformation(
                    haramImageDetector = HaramImageDetector(context),
                    blurRadiusPx = blurRadiusPx,
                )
            )
        }
        .build()

    ImageLoader(context).enqueue(request)
}
