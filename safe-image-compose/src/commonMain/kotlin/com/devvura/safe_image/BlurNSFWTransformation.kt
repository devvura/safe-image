package com.devvura.safe_image

import coil3.PlatformContext
import coil3.transform.Transformation

internal expect fun getBlurNSFWTransformation(
    onBlur: (isBlurred: Boolean) -> Unit,
    blurRadius: Int,
    context: PlatformContext
): Transformation