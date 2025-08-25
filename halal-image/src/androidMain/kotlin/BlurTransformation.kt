import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import coil3.size.Size
import coil3.transform.Transformation
import kotlin.math.max
import kotlin.math.min

/**
 * A reusable, thread‑safe box‑blur transformation for Coil.
 *
 * ● Encapsulates all blur logic in one class (Encapsulation)
 * ● Separates steps into small private functions (Single‑Responsibility)
 * ● Can be extended or composed with other strategies (Open for extension)
 */


internal open class BlurTransformation(
    private val blurRadiusPx: Int,
    private val downscaleFactor: Float = DEFAULT_SCALE,
    private val blurPasses: Int = DEFAULT_PASSES
) : Transformation() {

    companion object {
        const val MIN_RADIUS = 1
        const val MAX_RADIUS = 255
        const val DEFAULT_SCALE = 0.4f
        const val DEFAULT_PASSES = 3
    }

    override val cacheKey: String = "blur_${blurRadiusPx}_${downscaleFactor}_${blurPasses}"

    /**
     * Applies a blur transformation to the input [android.graphics.Bitmap].
     * This function is called by Coil during image loading and transformation.
     * The input bitmap is never mutated.
     *
     * @param input The original bitmap to be transformed.
     * @param size The desired size of the transformed image (unused here).
     * @return A new bitmap with the blur effect applied.
     */
    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val clampedRadius = blurRadiusPx.coerceIn(MIN_RADIUS, MAX_RADIUS)
        return createBlurredBitmap(input, clampedRadius)
    }

    /**
     * Handles the full blur pipeline: downscaling the original image,
     * applying a multi-pass box blur, then upscaling the result back to
     * the original size. Recycles intermediate bitmaps if safe.
     */
    private fun createBlurredBitmap(original: Bitmap, radius: Int): Bitmap {
        val scaledBitmap = downscaleBitmap(original)
        val blurredBitmap = applyBoxBlur(scaledBitmap, radius)
        val finalBitmap = upscaleBitmap(blurredBitmap, original.width, original.height)
        recycleIfNotSame(scaledBitmap, original)
        recycleIfNotSame(blurredBitmap, finalBitmap)
        return finalBitmap
    }

    private fun downscaleBitmap(bitmap: Bitmap): Bitmap {
        val newWidth = (bitmap.width * downscaleFactor).toInt().coerceAtLeast(1)
        val newHeight = (bitmap.height * downscaleFactor).toInt().coerceAtLeast(1)
        return if (newWidth == bitmap.width && newHeight == bitmap.height) bitmap
        else bitmap.scale(newWidth, newHeight, false)
    }

    private fun upscaleBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        return if (bitmap.width == targetWidth && bitmap.height == targetHeight) bitmap
        else bitmap.scale(targetWidth, targetHeight)
    }

    private fun recycleIfNotSame(candidate: Bitmap, reference: Bitmap) {
        if (candidate != reference && !candidate.isRecycled) candidate.recycle()
    }

    private fun applyBoxBlur(bitmap: Bitmap, radius: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val inputPixels = IntArray(width * height)
        val tempPixels = IntArray(width * height)

        bitmap.getPixels(inputPixels, 0, width, 0, 0, width, height)

        repeat(blurPasses) {
            blurHorizontally(inputPixels, tempPixels, width, height, radius)
            blurVertically(tempPixels, inputPixels, width, height, radius)
        }

        return createBitmap(width, height).apply {
            setPixels(inputPixels, 0, width, 0, 0, width, height)
        }
    }

    /**
     * Applies a horizontal box blur pass over the input pixel array.
     * For each pixel, it averages the values within a horizontal radius
     * and writes the result to the output array.
     */
    private fun blurHorizontally(
        input: IntArray,
        output: IntArray,
        width: Int,
        height: Int,
        radius: Int
    ) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                var alphaSum = 0
                var redSum = 0
                var greenSum = 0
                var blueSum = 0
                var pixelCount = 0

                val startX = max(0, x - radius)
                val endX = min(width - 1, x + radius)

                for (px in startX..endX) {
                    val pixel = input[y * width + px]
                    alphaSum += pixel ushr 24 and 0xFF
                    redSum += pixel ushr 16 and 0xFF
                    greenSum += pixel ushr 8 and 0xFF
                    blueSum += pixel and 0xFF
                    pixelCount++
                }

                output[y * width + x] = argb(
                    alphaSum / pixelCount,
                    redSum / pixelCount,
                    greenSum / pixelCount,
                    blueSum / pixelCount
                )
            }
        }
    }

    /**
     * Applies a vertical box blur pass over the input pixel array.
     * For each pixel, it averages the values within a vertical radius
     * and writes the result to the output array.
     */
    private fun blurVertically(
        input: IntArray,
        output: IntArray,
        width: Int,
        height: Int,
        radius: Int
    ) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                var alphaSum = 0
                var redSum = 0
                var greenSum = 0
                var blueSum = 0
                var pixelCount = 0

                val startY = max(0, y - radius)
                val endY = min(height - 1, y + radius)

                for (py in startY..endY) {
                    val pixel = input[py * width + x]
                    alphaSum += pixel ushr 24 and 0xFF
                    redSum += pixel ushr 16 and 0xFF
                    greenSum += pixel ushr 8 and 0xFF
                    blueSum += pixel and 0xFF
                    pixelCount++
                }

                output[y * width + x] = argb(
                    alphaSum / pixelCount,
                    redSum / pixelCount,
                    greenSum / pixelCount,
                    blueSum / pixelCount
                )
            }
        }
    }

    /**
     * Constructs an ARGB color integer from separate channel values.
     * Each channel is clamped to the range 0..255 before packing.
     *
     * @param alpha Alpha channel [0..255]
     * @param red Red channel [0..255]
     * @param green Green channel [0..255]
     * @param blue Blue channel [0..255]
     * @return Packed ARGB color integer
     */
    private fun argb(alpha: Int, red: Int, green: Int, blue: Int): Int {
        return (alpha.coerceIn(0, 255) shl 24) or
                (red.coerceIn(0, 255) shl 16) or
                (green.coerceIn(0, 255) shl 8) or
                blue.coerceIn(0, 255)
    }
}