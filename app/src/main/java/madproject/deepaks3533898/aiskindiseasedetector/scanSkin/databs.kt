package madproject.deepaks3533898.aiskindiseasedetector.scanSkin

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "scan_history")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val diseaseName: String,
    val confidence: Float,
    val date: Long,
    val note: String,
    val imagePath: String
)

@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_history ORDER BY date DESC")
    fun getAllScans(): Flow<List<ScanRecord>>

    @Insert
    suspend fun insertScan(scan: ScanRecord)

    @Delete
    suspend fun deleteScan(scan: ScanRecord)
}

@Database(entities = [ScanRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao
}