package madproject.deepaks3533898.aiskindiseasedetector

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.AccentGreen
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.CardWhite
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.FirstBG
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.LightBlue
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.PrimaryBlue
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.SoftBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val userName : String= PatientData.getName(context)!!

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(PrimaryBlue, FirstBG)
    )

    Scaffold(
        containerColor = SoftBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.ScanDisease.route)
                },
                containerColor = FirstBG
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
                                modifier = Modifier.size(30.dp).clickable{
                                    navController.navigate(Screen.Profile.route)
                                }
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
                                    navController.navigate(Screen.ScanHistory.route)

//                                    onHistoryClick
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 🔹 Health Tips Card
                    AutoSkinTipSlider()

                    // 🔹 Recent Scans
                    Text(
                        "More Actions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PremiumRecentItem(
                        title = "Diseases and Symptoms",
                        icon = Icons.Default.BackHand,
                        color = AccentGreen,
                        onItemClicked = {
                            navController.navigate(Screen.EduArticles.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PremiumRecentItem(
                        title = "Nearby Hospitals",
                        icon = Icons.Default.LocationOn,
                        color = AccentGreen,
                        onItemClicked = {
                            navController.navigate(Screen.NearbyHospitals.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PremiumRecentItem(
                        title = "About Us",
                        icon = Icons.Default.Info,
                        color = AccentGreen,
                        onItemClicked = {
//                            navController.navigate(Screen.NearbyHospitals.route)
                        }
                    )

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
fun PremiumRecentItem(
    icon: ImageVector,
    color: Color,
    title: String,
    onItemClicked: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ){
                Icon(icon, contentDescription = title, tint = color)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    title,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            IconButton(onClick = { onItemClicked.invoke() }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Back")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSkinTipSlider() {
    val pagerState = rememberPagerState(pageCount = { dailyTips.size })

    // Auto-scroll logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % dailyTips.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            val tip = dailyTips[page]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Text Section
                    Column(
                        modifier = Modifier
                            .weight(1.2f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${tip.icon} ${tip.title}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tip.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            lineHeight = 16.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Image Section
                    AsyncImage(
                        model = tip.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Pager Indicator (The Dots)
        Row(
            Modifier
                .height(24.dp)
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(dailyTips.size) { iteration ->
                val color = if (pagerState.currentPage == iteration)
                    MaterialTheme.colorScheme.primary
                else
                    Color.LightGray

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

data class SkinTip(
    val icon: String,
    val title: String,
    val description: String,
    val imageUrl: String
)

val dailyTips = listOf(
    SkinTip("☀️", "Sun Protection", "Apply SPF 30+ daily, even on cloudy days, to prevent premature aging.", "https://images.unsplash.com/photo-1521223344201-d169129f7b7d?q=80&w=500"),
    SkinTip("💧", "Stay Hydrated", "Drink at least 8 glasses of water daily to maintain skin elasticity.", "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?q=80&w=500"),
    SkinTip("🧼", "Double Cleanse", "Use an oil-based cleanser followed by water-based to remove deep impurities.", "https://images.unsplash.com/photo-1556229010-6c3f2c9ca5f8?q=80&w=500"),
    SkinTip("😴", "Beauty Sleep", "Your skin repairs itself at night. Aim for 7-9 hours of quality sleep.", "https://images.unsplash.com/photo-1520206151081-9bf94ee043ad?q=80&w=500"),
    SkinTip("🥗", "Skin Food", "Eat antioxidant-rich berries and fatty fish like salmon for a natural glow.", "https://images.unsplash.com/photo-1490818387583-1baba5e638af?q=80&w=500"),
    SkinTip("🧴", "Moisturize", "Apply moisturizer within 3 minutes of showering to lock in hydration.", "https://images.unsplash.com/photo-1570172619674-c6a81966ca3a?q=80&w=500"),
    SkinTip("🚫", "Hands Off", "Avoid touching your face to prevent the spread of bacteria and acne.", "https://images.unsplash.com/photo-1512446813987-440348abc035?q=80&w=500"),
    SkinTip("🛁", "Lukewarm Water", "Hot water strips natural oils. Wash your face with lukewarm water instead.", "https://images.unsplash.com/photo-1616391182219-e080b4d1043a?q=80&w=500"),
    SkinTip("📱", "Sanitize Phone", "Wipe your phone screen daily to prevent dirt transfer to your cheeks.", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=500"),
    SkinTip("🌿", "Silk Pillowcases", "Silk reduces friction on skin, preventing sleep lines and hair breakage.", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?q=80&w=500"),
    SkinTip("🍏", "Vitamin C", "Use a Vitamin C serum in the morning to brighten and protect against pollution.", "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=500"),
    SkinTip("🧘", "Manage Stress", "High cortisol levels can lead to breakouts. Practice deep breathing.", "https://images.unsplash.com/photo-1506126613408-eca07ce68773?q=80&w=500"),
    SkinTip("🕶️", "Protect Eyes", "Wear sunglasses to prevent squinting and fine lines (crow's feet).", "https://images.unsplash.com/photo-1473496169904-658ba7c44d8a?q=80&w=500"),
    SkinTip("🧪", "Patch Test", "Always patch test new skincare products for 24 hours before full use.", "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?q=80&w=500"),
    SkinTip("🍵", "Green Tea", "Drinking green tea provides polyphenols that help protect against UV damage.", "https://images.unsplash.com/photo-1564890369478-c89ca6d9cde9?q=80&w=500"),
    SkinTip("🧼", "Clean Brushes", "Wash makeup brushes weekly to prevent buildup of old oil and bacteria.", "https://images.unsplash.com/photo-1522338242992-e1a54906a8da?q=80&w=500"),
    SkinTip("🧣", "Neck Care", "Extend your skincare routine down to your neck and décolletage.", "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?q=80&w=500"),
    SkinTip("❄️", "Cool it down", "Store eye creams in the fridge to help de-puff eyes in the morning.", "https://images.unsplash.com/photo-1550133730-695473e544be?q=80&w=500"),
    SkinTip("🍬", "Less Sugar", "High sugar intake can trigger glycation, which damages skin collagen.", "https://images.unsplash.com/photo-1534002133490-9f94f92330a8?q=80&w=500"),
    SkinTip("🩺", "Annual Checkup", "Visit a dermatologist once a year for a full-body mole screening.", "https://images.unsplash.com/photo-1579684385127-1ef15d508118?q=80&w=500")
)