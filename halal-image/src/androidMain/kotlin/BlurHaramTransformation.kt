import android.graphics.Bitmap
import coil3.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BlurHaramTransformation(
    private val onBlur: (Boolean) -> Unit,
    private val haramImageDetector: HaramImageDetector,
    blurRadiusPx: Int,
    downscaleFactor: Float = DEFAULT_SCALE,
    blurPasses: Int = DEFAULT_PASSES
) : BlurTransformation(blurRadiusPx, downscaleFactor, blurPasses) {
    override suspend fun transform(input: Bitmap, size: Size): Bitmap =
        withContext(Dispatchers.Default) {
            val isImageSensitive = haramImageDetector.isImageHaram(input)
            if (isImageSensitive) {
                onBlur(true)
                super.transform(input, size)
            } else {
                onBlur(false)
                input
            }
        }
}