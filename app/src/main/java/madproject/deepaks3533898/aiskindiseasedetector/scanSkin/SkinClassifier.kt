package madproject.deepaks3533898.aiskindiseasedetector.scanSkin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.get
import androidx.core.graphics.scale
import com.google.android.gms.tflite.java.TfLite
import org.tensorflow.lite.InterpreterApi
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class SkinClassifier(private val context: Context) {


    private var interpreter: InterpreterApi? = null
    private var isInitialized = false
    val labels: List<String>

    private val inputSize = 224
    private val numChannels = 3
    private val numClasses = 5

    init {
        labels = context.assets.open("labels.txt")
            .bufferedReader()
            .readLines()

        TfLite.initialize(context).addOnSuccessListener {
            try {
                val model = loadModelFile()

                val options = InterpreterApi.Options()
                    .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)

                interpreter = InterpreterApi.create(model, options)
                isInitialized = true
                Log.d("TFLite_Classifier", "Interpreter initialized successfully from System Runtime")
            } catch (e: Exception) {
                Log.e("TFLite_Classifier", "Error creating interpreter: ${e.message}")
                e.printStackTrace()
            }
        }.addOnFailureListener { e ->
            Log.e("TFLite_Classifier", "TfLite initialization failed: ${e.message}")
        }
    }

    private fun loadModelFile(): ByteBuffer {
        val fileDescriptor = context.assets.openFd("skin_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }


    fun classify(bitmap: Bitmap): Pair<String, Float>? {
        val TAG = "TFLite_Classifier"

        if (!isInitialized || interpreter == null) {
            Log.e(TAG, "Classification failed: Interpreter not ready. isInitialized=$isInitialized")
            return null
        }

        val startTime = System.currentTimeMillis()

        Log.d(TAG, "Starting classification. Original Bitmap: ${bitmap.width}x${bitmap.height}")

        val resized = bitmap.scale(inputSize, inputSize)
        Log.d(TAG, "Bitmap resized to: ${resized.width}x${resized.height}")

        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * numChannels)
        byteBuffer.order(ByteOrder.nativeOrder())

        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                val pixel = resized[x, y]
                byteBuffer.putFloat(Color.red(pixel) / 255f)
                byteBuffer.putFloat(Color.green(pixel) / 255f)
                byteBuffer.putFloat(Color.blue(pixel) / 255f)
            }
        }

        val output = Array(1) { FloatArray(numClasses) }
        try {
            interpreter?.run(byteBuffer, output)
        } catch (e: Exception) {
            Log.e(TAG, "Inference error: ${e.message}")
            return null
        }

        val result = output[0]

        Log.d(TAG, "Raw Output Scores: ${result.joinToString(", ")}")

        val maxIndex = result.indices.maxByOrNull { result[it] } ?: 0
        val confidence = result[maxIndex]
        val label = labels.getOrElse(maxIndex) { "Unknown" }

        val endTime = System.currentTimeMillis()
        Log.i(TAG, "Result: $label (${confidence * 100}%) | Time: ${endTime - startTime}ms")

        return Pair(label, confidence)
    }

    fun close() {
        interpreter?.close()
    }
}