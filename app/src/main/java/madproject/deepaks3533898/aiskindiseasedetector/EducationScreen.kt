package madproject.deepaks3533898.aiskindiseasedetector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.Serializable
import madproject.deepaks3533898.aiskindiseasedetector.scanSkin.ScanViewModel
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.FirstBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EducationScreen(onBackClick: () -> Unit,navController: NavController,viewModel: ScanViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Fungal", "Bacterial", "Viral", "Acne", "Allergic")

    val filteredArticles = skinArticles.filter {
        (selectedCategory == "All" || it.type == selectedCategory) &&
                (it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skin Library", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = FirstBG
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search diseases or symptoms...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            LazyRow(
                modifier = Modifier.padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredArticles) { article ->
                    ArticleCard(article,navController,viewModel)
                }
            }
        }
    }
}

@Composable
fun ArticleCard(article: SkinArticle,navController: NavController,viewModel: ScanViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = article.type,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    viewModel.selectedArticle = article
                    navController.navigate("full_article")

                          },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Read Full Guide", fontWeight = FontWeight.Bold)
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}

@Serializable
data class SkinArticle(
    val title: String,
    val type: String,
    val content: String,
    val fullDescription: String,
    val treatments: List<String>,
    val symptoms: List<String>
)

val skinArticles = listOf(
    SkinArticle(
        title = "Ringworm (Tinea Corporis)",
        type = "Fungal",
        content = "A red, itchy, circular rash with clearer skin in the middle.",
        fullDescription = "Despite the name, ringworm is caused by a fungus, not a worm. It is highly contagious and can spread through skin contact or by sharing towels and bedding. The fungus thrives in warm, moist areas.",
        symptoms = listOf("Circular rash with raised edges", "Itchy or scaly skin", "Clearer skin in the center of the ring", "Overlapping rings"),
        treatments = listOf("Over-the-counter antifungal creams (Clotrimazole)", "Keeping skin clean and dry", "Washing sheets and clothes daily", "Prescription oral antifungals for severe cases")
    ),
    SkinArticle(
        title = "Impetigo",
        type = "Bacterial",
        content = "Highly contagious sores that develop honey-colored crusts.",
        fullDescription = "Impetigo is a bacterial infection usually caused by Staph or Strep bacteria. It often enters the skin through small cuts or insect bites. While it looks alarming, it is usually not painful, though it can be very itchy.",
        symptoms = listOf("Red sores that pop easily", "Honey-colored crusts", "Fluid-filled blisters", "Itchiness and soreness around the nose and mouth"),
        treatments = listOf("Mupirocin antibiotic ointment", "Gentle cleansing of the sores", "Covering affected areas with bandages", "Oral antibiotics if the infection spreads")
    ),
    SkinArticle(
        title = "Herpes Simplex (Cold Sores)",
        type = "Viral",
        content = "Tiny, fluid-filled blisters often appearing in clusters around the mouth.",
        fullDescription = "Cold sores are caused by the Herpes Simplex Virus (HSV-1). Once infected, the virus remains dormant in nerve cells and can be triggered by stress, sunlight, or a weakened immune system.",
        symptoms = listOf("Tingling or burning sensation before blisters appear", "Small fluid-filled blisters", "Oozing and crusting", "Fever or sore throat (during first outbreak)"),
        treatments = listOf("Antiviral creams (Docosanol)", "Prescription oral antivirals (Valacyclovir)", "Ice packs for swelling", "Avoiding sharing lip balms or utensils")
    ),
    SkinArticle(
        title = "Cystic Acne",
        type = "Acne",
        content = "Painful, inflamed lumps that develop deep under the skin.",
        fullDescription = "Cystic acne is the most severe form of acne. It occurs when oil and dead skin cells build up deep within hair follicles, leading to an infection that causes large, painful cysts that can lead to permanent scarring.",
        symptoms = listOf("Large, painful red lumps", "Cysts that feel like they are under the skin", "Lack of a visible 'head'", "Tenderness in the affected area"),
        treatments = listOf("Dermatologist-prescribed Isotretinoin", "Oral antibiotics", "Steroid injections for single large cysts", "Strict non-comedogenic skincare routine")
    ),
    SkinArticle(
        title = "Contact Dermatitis",
        type = "Allergic",
        content = "A red, itchy rash caused by direct contact with a specific substance.",
        fullDescription = "Contact dermatitis is an inflammatory skin reaction. It can be caused by an irritant (like harsh soap) or an allergen (like nickel or poison ivy). The reaction can happen immediately or hours after contact.",
        symptoms = listOf("Red, itchy rash", "Dry, cracked, or scaly skin", "Bumps and blisters", "Burning or stinging sensation"),
        treatments = listOf("Identifying and avoiding the trigger", "Corticosteroid creams", "Cool, wet compresses", "Oral antihistamines for itching")
    ),
    SkinArticle(
        title = "Athlete's Foot",
        type = "Fungal",
        content = "A fungal infection that usually begins between the toes.",
        fullDescription = "Athlete's foot (tinea pedis) is a fungal infection that thrives in the damp environment of shoes and socks. It is common in athletes but can affect anyone who frequents public showers or locker rooms.",
        symptoms = listOf("Scaly, red rash between toes", "Itching immediately after taking off shoes", "Small blisters", "Chronic dryness on the soles"),
        treatments = listOf("Antifungal sprays or powders", "Keeping feet dry and clean", "Changing socks frequently", "Wearing breathable shoes")
    ),
    SkinArticle(
        title = "Psoriasis",
        type = "Allergic",
        content = "A chronic autoimmune condition causing silver, scaly patches.",
        fullDescription = "Psoriasis speeds up the life cycle of skin cells, causing them to build up on the surface. These patches are actually areas of inflammation where the body is attacking its own healthy tissue.",
        symptoms = listOf("Silvery scales (plaques)", "Dry, cracked skin", "Itching or burning", "Thickened or pitted nails"),
        treatments = listOf("Topical corticosteroids", "Phototherapy (UV light)", "Biologics for severe cases", "Salicylic acid for scaling")
    ),
    SkinArticle(
        title = "Shingles",
        type = "Viral",
        content = "A painful stripe of blisters caused by the chickenpox virus.",
        fullDescription = "If you have had chickenpox, the virus stays in your body. Years later, it can reactivate as Shingles. It follows a nerve path, which is why it usually only appears on one side of the body.",
        symptoms = listOf("Intense burning or stabbing pain", "Fluid-filled blisters", "Rash that follows a nerve line", "Extreme sensitivity to touch"),
        treatments = listOf("Early antiviral treatment", "Pain management medication", "Calamine lotion", "Keeping the rash covered and clean")
    ),
    SkinArticle(
        title = "Cellulitis",
        type = "Bacterial",
        content = "A serious bacterial infection affecting deep skin layers.",
        fullDescription = "Cellulitis is a deep skin infection that can be dangerous if not treated quickly. It often occurs in the lower legs and requires medical attention to prevent it from entering the bloodstream.",
        symptoms = listOf("Expanding red area of skin", "Swelling and warmth", "Pain and tenderness", "Fever or chills"),
        treatments = listOf("Immediate course of oral antibiotics", "Elevating the affected area", "Pain relievers", "Monitoring for spreading (marking the area)")
    ),
    SkinArticle(
        title = "Rosacea",
        type = "Acne",
        content = "Chronic facial redness often mistaken for acne.",
        fullDescription = "Rosacea is a common condition that causes blushing and visible blood vessels. While it looks like acne, standard acne treatments can actually make rosacea worse because the skin is extremely sensitive.",
        symptoms = listOf("Facial flushing", "Visible spider veins", "Swollen red bumps", "Eye irritation (Ocular rosacea)"),
        treatments = listOf("Metronidazole gel", "Laser therapy for blood vessels", "Identifying triggers (sun, spice, heat)", "Gentle, fragrance-free skincare")
    ),
    SkinArticle(
        title = "Eczema",
        type = "Allergic",
        content = "Red, itchy skin common in people with sensitive immune systems.",
        fullDescription = "Atopic Dermatitis (Eczema) is a condition that makes your skin's protective barrier weak. This allows moisture to escape and irritants to get in, leading to chronic inflammation.",
        symptoms = listOf("Intense itching", "Red to brownish-gray patches", "Thickened, leathery skin", "Small, raised bumps that leak fluid"),
        treatments = listOf("Fragrance-free thick moisturizers", "Topical steroid creams", "Wet wrap therapy", "Avoiding wool and harsh detergents")
    )
)