package madproject.deepaks3533898.aiskindiseasedetector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.AccentGreen
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.CardWhite
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.LightBlue
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.PrimaryBlue
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.SoftBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    val userName : String="Deepak"

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlue, LightBlue)
    )

    Scaffold(
        containerColor = SoftBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    onScanClick
                },
                containerColor = PrimaryBlue
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Scan")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // 🔹 HEADER WITH GRADIENT
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(gradientBrush)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    "Welcome 👋",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    userName,
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            "AI-powered skin analysis at your fingertips",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // 🔹 OVERLAPPING CARD SECTION
            item {
                Column(
                    modifier = Modifier
                        .offset(y = (-30).dp)
                        .padding(horizontal = 16.dp)
                ) {

                    // Quick Actions Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {

                            PremiumActionItem(
                                title = "Scan",
                                icon = Icons.Default.CameraAlt,
                                color = PrimaryBlue,
                                onClick = {
                                    navController.navigate(Screen.ScanDisease.route)
//                                    onScanClick
                                }
                            )

                            PremiumActionItem(
                                title = "History",
                                icon = Icons.Default.History,
                                color = AccentGreen,
                                onClick = {
//                                    onHistoryClick
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔹 Health Tips Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "💡 Daily Skin Tip",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Apply sunscreen SPF 30+ daily to prevent skin damage and aging.",
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔹 Recent Scans
                    Text(
                        "Recent Scans",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    repeat(3) {
                        PremiumRecentItem()
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumActionItem(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = color)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(title, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PremiumRecentItem() {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    "Acne",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Confidence: 87%",
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "2 Apr 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}