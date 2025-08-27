package com.devfares.halal_view

import android.graphics.Bitmap
import coil3.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlurHaramTransformation(
    private val haramImageDetector: HaramImageDetector,
    blurRadiusPx: Int,
    downscaleFactor: Float = DEFAULT_SCALE,
    blurPasses: Int = DEFAULT_PASSES
) : BlurTransformation(blurRadiusPx, downscaleFactor, blurPasses) {
    override suspend fun transform(input: Bitmap, size: Size): Bitmap =
        withContext(Dispatchers.Default) {
            val isImageSensitive = haramImageDetector.isImageHaram(input)
            if (isImageSensitive) {
                super.transform(input, size)
            } else {
                input
            }
        }
}