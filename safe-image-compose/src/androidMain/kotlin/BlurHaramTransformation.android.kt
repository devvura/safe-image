import coil3.Bitmap
import coil3.PlatformContext
import coil3.size.Size
import coil3.transform.Transformation

internal actual fun getBlurNSFWTransformation(
    onBlur: (isBlurred: Boolean) -> Unit,
    blurRadiusPx: Int,
    context: PlatformContext
): Transformation = BlurHaramTransformation(
    onBlur = onBlur,
    haramImageDetector = HaramImageDetector(context),
    blurRadiusPx = blurRadiusPx
)

