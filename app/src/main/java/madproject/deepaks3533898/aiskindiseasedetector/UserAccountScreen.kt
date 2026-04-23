package madproject.deepaks3533898.aiskindiseasedetector

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.FirstBG


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userEmail: String) {

    val context = LocalContext.current
    val db = FirebaseDatabase.getInstance().getReference("PatientAccounts")

    var isEditing by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    var userName by remember { mutableStateOf("") }
    var userAge by remember { mutableStateOf("") }
    var userWeight by remember { mutableStateOf("") }
    var userGender by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    // 🔹 Load Data
    LaunchedEffect(Unit) {
        db.child(userEmail.replace(".", ","))
            .get()
            .addOnSuccessListener {
                val data = it.getValue(PersonDetails::class.java)
                data?.let {
                    userName = it.name
                    userAge = it.age
                    userWeight = it.weight
                    userGender = it.gender
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FirstBG)
                .padding(padding)
                .verticalScroll(scrollState)
                .imePadding(), // 🔥 handles keyboard
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // 👤 Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFF1565C0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1).uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileField("Name", userName, isEditing) { userName = it }
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileField("Age", userAge, isEditing) { userAge = it }
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileField("Weight", userWeight, isEditing) { userWeight = it }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Gender
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gender", fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            GenderOption("Male", userGender, isEditing) { userGender = it }
                            GenderOption("Female", userGender, isEditing) { userGender = it }
//                            GenderOption("Other", userGender, isEditing) { userGender = it }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Edit / Save
                    Button(
                        onClick = {
                            if (isEditing) {
                                val updates = mapOf(
                                    "name" to userName,
                                    "age" to userAge,
                                    "weight" to userWeight,
                                    "gender" to userGender
                                )

                                db.child(userEmail.replace(".", ","))
                                    .updateChildren(updates)
                                    .addOnSuccessListener {
                                        showSuccessDialog = true
                                        isEditing = false
                                    }
                            } else {
                                isEditing = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (isEditing) "Save Changes" else "Edit Profile")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Logout
                    OutlinedButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Logout", color = Color.Red)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Success") },
            text = { Text("Profile updated successfully!") }
        )
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                Button(onClick = {
                    showLogoutDialog = false
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") }
        )
    }
}


@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    onChange: (String) -> Unit
) {
    Column {
        Text(label, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(4.dp))

        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = value,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F3F6), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun GenderOption(
    gender: String,
    selectedGender: String,
    isEditing: Boolean,
    onSelect: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        RadioButton(
            selected = selectedGender == gender,
            onClick = { if (isEditing) onSelect(gender) },
            enabled = isEditing
        )
        Text(gender)
    }
}