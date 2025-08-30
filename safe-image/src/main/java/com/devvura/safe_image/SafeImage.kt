package com.devvura.safe_image

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.toBitmap
import com.devvura.safe_image.util.setDrawableRes

fun ImageView.loadSafeImage(
    model: Any,
    blurRadius: Int = 50,
    onLoading: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    crossFadeEnabled: Boolean = true,
    @DrawableRes placeholderRes: Int? = null,
    @DrawableRes errorDrawableRes: Int? = null
) {
    val context = this.context
    val request = ImageRequest.Builder(context)
        .data(model)
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
                BlurNSFWTransformation(
                    nsfwImageDetector = NSFWImageDetector(context),
                    blurRadius = blurRadius,
                )
            )
        }
        .build()

    ImageLoader(context).enqueue(request)
}
