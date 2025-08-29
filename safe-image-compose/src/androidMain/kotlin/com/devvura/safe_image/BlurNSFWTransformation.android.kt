package com.devvura.safe_image

import coil3.PlatformContext
import coil3.transform.Transformation

internal actual fun getBlurNSFWTransformation(
    onBlur: (isBlurred: Boolean) -> Unit,
    blurRadius: Int,
    context: PlatformContext
): Transformation = BlurNSFWTransformation(
    onBlur = onBlur,
    nsfwImageDetector = NSFWImageDetector(context),
    blurRadius = blurRadius
)

