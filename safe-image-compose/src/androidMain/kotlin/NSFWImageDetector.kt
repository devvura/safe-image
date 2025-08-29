import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

internal class NSFWImageDetector(
    context: Context
) {
    private val inputImageSize = INPUT_IMAGE_SIZE
    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context, MODEL_FILE))
    }

    fun isImageNSFW(
        selectedBitmap: Bitmap,
    ): Boolean {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(selectedBitmap)

        val processedImage = buildImageProcessor().process(tensorImage)

        val inputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, inputImageSize, inputImageSize, 3),
            DataType.FLOAT32
        )
        inputBuffer.loadBuffer(processedImage.buffer)

        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, 2),
            DataType.FLOAT32
        )

        interpreter.run(inputBuffer.buffer, outputBuffer.buffer.rewind())

        val result = outputBuffer.floatArray
        val nsfwScore = result.getOrNull(1) ?: 0f
        return nsfwScore > 0.5f
    }

    private fun buildImageProcessor(): ImageProcessor {
        return ImageProcessor.Builder()
            .add(ResizeOp(inputImageSize, inputImageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
    }

    companion object {
        private const val INPUT_IMAGE_SIZE = 224
        private const val MODEL_FILE = "Islamic_Image_Model.tflite"
    }
}

/**
 * Helper to load the TFLite model from assets.
 */
private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
    val assetFileDescriptor = context.assets.openFd(modelName)
    assetFileDescriptor.use { afd ->
        val inputStream = afd.createInputStream()
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            afd.startOffset,
            afd.declaredLength
        )
    }
}