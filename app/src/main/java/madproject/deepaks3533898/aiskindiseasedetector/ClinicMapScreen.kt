package madproject.deepaks3533898.aiskindiseasedetector

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.FirstBG

data class Clinic(
    val name: String,
    val address: String,
    val latLng: LatLng,
    val rating: Double,
    val phoneNumber: String,
    val imageUrl: String,
    val description: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicMapScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val teessideLocation = LatLng(54.5714, -1.2334)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(teessideLocation, 14f)
    }

    var selectedClinic by remember { mutableStateOf<Clinic?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (isGranted) {
            checkAndEnableGPS(context)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            checkAndEnableGPS(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clinics Near by", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FirstBG)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission,
                    mapStyleOptions = null // You can add custom JSON styling here for premium dark/silver mode
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false,
                    compassEnabled = true
                ),
                onMapClick = { showSheet = false }
            ) {
                getMockClinics(teessideLocation).forEach { clinic ->
                    Marker(
                        state = MarkerState(position = clinic.latLng),
                        title = clinic.name,
                        onClick = {
                            selectedClinic = clinic
                            showSheet = true
                            false
                        }
                    )
                }
            }

            if (showSheet && selectedClinic != null) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    ClinicDetailContent(selectedClinic!!, context)
                }
            }
        }
    }
}

private fun checkAndEnableGPS(context: Context) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val task = client.checkLocationSettings(builder.build())

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val activity = context as? androidx.activity.ComponentActivity
                exception.startResolutionForResult(activity!!, 1001)
            } catch (sendEx: Exception) {
                // Ignore
            }
        }
    }
}

@Composable
fun ClinicDetailContent(clinic: Clinic, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .navigationBarsPadding()
    ) {
        AsyncImage(
            model = clinic.imageUrl,
            contentDescription = clinic.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = clinic.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 28.sp
                )
                Text(
                    text = clinic.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text(" ${clinic.rating}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = clinic.description,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${clinic.phoneNumber}"))
                    context.startActivity(intent)
                },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Call Now", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    val gmmIntentUri = Uri.parse("google.navigation:q=${clinic.latLng.latitude},${clinic.latLng.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Directions", fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun getMockClinics(center: LatLng): List<Clinic> {
    return listOf(
        Clinic(
            name = "The James Cook University Hospital - Dermatology",
            address = "Marton Rd, Middlesbrough TS4 3BW",
            latLng = LatLng(54.5532, -1.2135),
            rating = 4.2,
            phoneNumber = "01642 282424",
            imageUrl = "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=800",
            description = "Main NHS specialist dermatology department for South Tees, offering clinics for skin cancer, eczema, and psoriasis."
        ),
        Clinic(
            name = "sk:n Middlesbrough",
            address = "The James Cook University Hospital, Marton Rd, Middlesbrough TS4 3BW",
            latLng = LatLng(54.5535, -1.2140),
            rating = 4.8,
            phoneNumber = "01642 854000",
            imageUrl = "https://images.unsplash.com/photo-1629909613654-28e377c37b09?q=80&w=800",
            description = "Leading private skin clinic offering laser hair removal, mole mapping, and acne treatments."
        ),
        Clinic(
            name = "Tees Valley Hospital (Ramsay Health)",
            address = "Church Lane, Acklam, Middlesbrough TS5 7DX",
            latLng = LatLng(54.5510, -1.2680),
            rating = 4.5,
            phoneNumber = "01642 929409",
            imageUrl = "https://images.unsplash.com/photo-1586773860418-d3b978ec017e?q=80&w=800",
            description = "Private hospital specializing in medical dermatology, diagnostic biopsies, and minor skin surgery."
        ),
        Clinic(
            name = "Nuffield Health Tees Hospital",
            address = "Junction Rd, Norton, Stockton-on-Tees TS20 1PX",
            latLng = LatLng(54.5900, -1.3320),
            rating = 4.7,
            phoneNumber = "01642 360100",
            imageUrl = "https://images.unsplash.com/photo-1512678080530-7760d81faba6?q=80&w=800",
            description = "Offers expert consultant-led dermatology services including skin cancer screening and mole removal."
        ),
        Clinic(
            name = "Acklam Medical Centre",
            address = "Brimham Ct, Acklam, Middlesbrough TS5 8PZ",
            latLng = LatLng(54.5420, -1.2650),
            rating = 3.9,
            phoneNumber = "01642 818200",
            imageUrl = "https://images.unsplash.com/photo-1505751172876-fa1923c5c528?q=80&w=800",
            description = "General practice offering skin consultations and minor lesion monitoring."
        ),
        Clinic(
            name = "The Skin Clinic (Suzy's Skin Clinic)",
            address = "274 Linthorpe Rd, Middlesbrough TS1 3QS",
            latLng = LatLng(54.5680, -1.2380),
            rating = 4.9,
            phoneNumber = "07850 456789",
            imageUrl = "https://images.unsplash.com/photo-1616391182219-e080b4d1043a?q=80&w=800",
            description = "Specialized aesthetics and skin health clinic focusing on melasma and advanced skin rejuvenation."
        ),
        Clinic(
            name = "Borough Road Foot & Skin Clinic",
            address = "129 Borough Rd, Middlesbrough TS1 3AN",
            latLng = LatLng(54.5735, -1.2355),
            rating = 4.3,
            phoneNumber = "01642 245999",
            imageUrl = "https://images.unsplash.com/photo-1576091160550-2173dba999ef?q=80&w=800",
            description = "Specializing in dermatological issues related to lower limbs and general skin health."
        ),
        Clinic(
            name = "Cleveland Health Centre",
            address = "20 Cleveland Square, Middlesbrough TS1 2NX",
            latLng = LatLng(54.5760, -1.2330),
            rating = 4.0,
            phoneNumber = "01642 242192",
            imageUrl = "https://images.unsplash.com/photo-1532938911079-1b06ac7ceec7?q=80&w=800",
            description = "City centre healthcare hub providing community dermatology referrals."
        ),
        Clinic(
            name = "One Wellness & Spa Skin Clinic",
            address = "Gloucester St, Middlesbrough TS1 5RE",
            latLng = LatLng(54.5705, -1.2450),
            rating = 4.6,
            phoneNumber = "01642 226111",
            imageUrl = "https://images.unsplash.com/photo-1515377905703-c4788e51af15?q=80&w=800",
            description = "Integrated wellness centre offering holistic skin treatments and dermatological facials."
        ),
        Clinic(
            name = "Linthorpe Surgery",
            address = "378 Linthorpe Rd, Middlesbrough TS5 6HA",
            latLng = LatLng(54.5615, -1.2420),
            rating = 4.1,
            phoneNumber = "01642 816823",
            imageUrl = "https://images.unsplash.com/photo-1471864190281-a93a3070b6de?q=80&w=800",
            description = "Provides primary care skin assessments and cryotherapy services."
        ),
        Clinic(
            name = "University Health Centre",
            address = "Teesside University, Southfield Rd, Middlesbrough TS1 3BA",
            latLng = LatLng(54.5710, -1.2325),
            rating = 4.4,
            phoneNumber = "01642 342125",
            imageUrl = "https://images.unsplash.com/photo-1579684385127-1ef15d508118?q=80&w=800",
            description = "Student health centre providing skin infection treatment and allergy advice."
        ),
        Clinic(
            name = "Park Surgery",
            address = "164 Linthorpe Rd, Middlesbrough TS1 3RB",
            latLng = LatLng(54.5700, -1.2370),
            rating = 3.8,
            phoneNumber = "01642 244634",
            imageUrl = "https://images.unsplash.com/photo-1527613426441-4da17471b66d?q=80&w=800",
            description = "Local GP clinic supporting patients with chronic skin conditions like acne and rosacea."
        ),
        Clinic(
            name = "Holmwood Surgery",
            address = "The Garth, Coulby Newham, Middlesbrough TS8 0UB",
            latLng = LatLng(54.5150, -1.2200),
            rating = 4.2,
            phoneNumber = "01642 590001",
            imageUrl = "https://images.unsplash.com/photo-1551601651-2a8555f1a136?q=80&w=800",
            description = "Offers minor surgery for skin tag and cyst removal in the Coulby Newham area."
        ),
        Clinic(
            name = "The Endeavour Practice",
            address = "Acklam Rd, Middlesbrough TS5 4AB",
            latLng = LatLng(54.5620, -1.2580),
            rating = 4.0,
            phoneNumber = "01642 817586",
            imageUrl = "https://images.unsplash.com/photo-1581056771107-24ca5f033842?q=80&w=800",
            description = "Community clinic offering dermatological check-ups and eczema management."
        ),
        Clinic(
            name = "Discovery House Dermatology Outpatients",
            address = "James Cook University Hospital, Middlesbrough TS4 3BW",
            latLng = LatLng(54.5528, -1.2145),
            rating = 4.3,
            phoneNumber = "01642 850850",
            imageUrl = "https://images.unsplash.com/photo-1538108197017-c1a9667393f7?q=80&w=800",
            description = "Specialist outpatient unit for phototherapy and chronic skin disease management."
        ),
        Clinic(
            name = "Martonside Medical Centre",
            address = "Marton Rd, Middlesbrough TS4 3BU",
            latLng = LatLng(54.5570, -1.2220),
            rating = 4.1,
            phoneNumber = "01642 816616",
            imageUrl = "https://images.unsplash.com/photo-1666214280557-f1b5022eb634?q=80&w=800",
            description = "Health centre providing diagnostic skin services near the Marton area."
        ),
        Clinic(
            name = "The Live Well Centre",
            address = "Dundas Shopping Centre, Middlesbrough TS1 1HR",
            latLng = LatLng(54.5770, -1.2360),
            rating = 4.5,
            phoneNumber = "01642 727572",
            imageUrl = "https://images.unsplash.com/photo-1504813184591-01572f98c85f?q=80&w=800",
            description = "Public health hub offering preventative advice for skin cancer and healthy living."
        ),
        Clinic(
            name = "Erimus Medical Centre",
            address = "Clevegate, Acklam, Middlesbrough TS5 8SZ",
            latLng = LatLng(54.5440, -1.2610),
            rating = 3.7,
            phoneNumber = "01642 817345",
            imageUrl = "https://images.unsplash.com/photo-1551076805-e1869033e561?q=80&w=800",
            description = "Primary care centre specializing in infectious skin disease management."
        ),
        Clinic(
            name = "Woodlands Road Surgery",
            address = "6 woodlands Rd, Middlesbrough TS1 3BE",
            latLng = LatLng(54.5718, -1.2365),
            rating = 3.9,
            phoneNumber = "01642 242033",
            imageUrl = "https://images.unsplash.com/photo-1582750433449-648ed127bb54?q=80&w=800",
            description = "Located near Teesside University, providing rapid care for acute skin rashes."
        ),
        Clinic(
            name = "Carter Bequest Health Centre",
            address = "Cambridge Rd, Linthorpe, Middlesbrough TS5 5NW",
            latLng = LatLng(54.5620, -1.2520),
            rating = 4.4,
            phoneNumber = "01642 850811",
            imageUrl = "https://images.unsplash.com/photo-1516549655169-df83a0774514?q=80&w=800",
            description = "Community health facility hosting visiting dermatology specialists and minor procedures."
        )
    )
}