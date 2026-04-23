package madproject.deepaks3533898.aiskindiseasedetector.scanSkin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.util.CoilUtils.result
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.FirstBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: ScanViewModel,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    var showNoteDialog by remember { mutableStateOf(false) }
    var userNote by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) } // New state

    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text("Add a Note") },
            text = {
                OutlinedTextField(
                    value = userNote,
                    onValueChange = { userNote = it },
                    placeholder = { Text("e.g., Noticed this 2 weeks ago...") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    // We pass the note AND the imageUri that was analyzed
                    // Assuming your ViewModel holds the uri used for the scan
                    viewModel.saveToDatabase(userNote)
                    showNoteDialog = false

                    showSuccessDialog = true
                }) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50), // Premium Green color
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Saved Successfully") },
            text = {
                Text("Your scan and notes have been added to your history.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        // Optional: Reset note for next time
                        userNote = ""
                    }
                ) {
                    Text("Okay", fontWeight = FontWeight.ExtraBold)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Analysis Detail",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FirstBG
                )
            )
        },
        bottomBar = {
            // Premium Action Button for Saving
            Button(
                onClick = {
                    showNoteDialog = true
                    /* We will implement database logic here next */
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Icon(Icons.Default.Done, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Scan to History", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            if (viewModel.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            } else if (viewModel.resultIndex != -1) {

                Spacer(modifier = Modifier.height(16.dp))

                // --- Result Card (Premium Look) ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.resultLabel,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50),
                        ) {
                            Text(
                                text = "Confidence: %.1f%%".format(viewModel.resultConfidence * 100),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Detailed Information Sections ---
                val idx = viewModel.resultIndex
                ResultDetailSection("Symptoms", getSymptoms(idx), Icons.Default.Info)
                ResultDetailSection("Possible Causes", getCauses(idx), Icons.Default.Warning)
                ResultDetailSection("Precautions", getPrecautions(idx), Icons.Default.CheckCircle)
                ResultDetailSection(
                    "Suggested Treatment",
                    getTreatment(idx),
                    Icons.Default.Favorite
                )

                Spacer(modifier = Modifier.height(100.dp)) // Space for bottom button

            } else {
//                EmptyStateView()
            }
        }
    }
}

@Composable
fun ResultDetailSection(title: String, content: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

fun getDiseaseName(index: Int): String = listOf(
    "Acne", "Eczema", "Psoriasis", "Ringworm", "Healthy"
)[index]

fun getSymptoms(index: Int): String = listOf(
    "Pimples, redness",
    "Dry itchy skin",
    "Thick scaly patches",
    "Circular itchy rash",
    "No symptoms"
)[index]

fun getCauses(index: Int): String = listOf(
    "Oil buildup",
    "Allergic reaction",
    "Immune disorder",
    "Fungal infection",
    "Healthy skin"
)[index]

fun getPrecautions(index: Int): String = listOf(
    "Wash face regularly",
    "Avoid harsh soaps",
    "Moisturize daily",
    "Avoid sharing clothes",
    "Maintain hygiene"
)[index]

fun getTreatment(index: Int): String = listOf(
    "Use mild cleanser",
    "Apply medicated cream",
    "Consult doctor",
    "Use antifungal cream",
    "No treatment needed"
)[index]