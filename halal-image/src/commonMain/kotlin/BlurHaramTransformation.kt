import coil3.PlatformContext
import coil3.transform.Transformation

internal expect fun getBlurHaramTransformation(
    onBlur: (isBlurred: Boolean) -> Unit,
    blurRadiusPx: Int,
    context: PlatformContext
): Transformation