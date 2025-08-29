package com.devvura.safe_image

import android.graphics.Bitmap
import coil3.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlurNSFWTransformation(
    private val nsfwImageDetector: NSFWImageDetector,
    blurRadius: Int,
    downscaleFactor: Float = DEFAULT_SCALE,
    blurPasses: Int = DEFAULT_PASSES
) : BlurTransformation(blurRadius, downscaleFactor, blurPasses) {
    override suspend fun transform(input: Bitmap, size: Size): Bitmap =
        withContext(Dispatchers.Default) {
            val isImageNSFW = nsfwImageDetector.isImageNSFW(input)
            if (isImageNSFW) {
                super.transform(input, size)
            } else {
                input
            }
        }
}