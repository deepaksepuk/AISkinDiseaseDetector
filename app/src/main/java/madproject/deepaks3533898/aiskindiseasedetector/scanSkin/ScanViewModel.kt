package madproject.deepaks3533898.aiskindiseasedetector.scanSkin

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import madproject.deepaks3533898.aiskindiseasedetector.SkinArticle
import kotlin.jvm.java

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    var isLoading by mutableStateOf(false)
    var resultLabel by mutableStateOf("")
    var resultConfidence by mutableFloatStateOf(0f)
    var resultIndex by mutableStateOf(-1)

    var isAnalysisComplete by mutableStateOf(false)

    var statusMessage by mutableStateOf("")

    var currentImageUri by mutableStateOf<Uri?>(null)

    private val classifier = SkinClassifier(application)

    fun analyzeImage(context: Context, uri: Uri) {

        currentImageUri = uri

        isLoading = true
        statusMessage = ""

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )

            val classification = classifier.classify(bitmap)

            if (classification != null) {
                val (label, confidence) = classification
                resultLabel = label
                resultConfidence = confidence
                resultIndex = classifier.labels.indexOf(classification.first)
                isAnalysisComplete = true
            } else {
                statusMessage = "TFLite is still initializing. Please try again in a moment."
            }

        } catch (e: Exception) {
            e.printStackTrace()
            statusMessage = "Error processing image."
        } finally {
            isLoading = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }


    private val db by lazy {
        Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "skin_disease_db"
        ).build()
    }

    private val dao = db.scanDao()

    val allScans = dao.getAllScans()


    fun saveToDatabase(note: String) {
        if (resultIndex == -1) return

        viewModelScope.launch(Dispatchers.IO) {
            val record = ScanRecord(
                diseaseName = resultLabel,
                confidence = resultConfidence,
                date = System.currentTimeMillis(),
                note = note,
                imagePath = currentImageUri?.toString() ?: ""
            )

            dao.insertScan(record)

            Log.d("Database", "Saved record for: ${record.diseaseName}")
        }
    }

    fun deleteRecord(record: ScanRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteScan(record)
        }
    }

    var selectedArticle by mutableStateOf<SkinArticle?>(null)
}