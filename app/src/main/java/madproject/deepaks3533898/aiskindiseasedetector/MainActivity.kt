package madproject.deepaks3533898.aiskindiseasedetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import madproject.deepaks3533898.aiskindiseasedetector.scanSkin.HistoryScreen
import madproject.deepaks3533898.aiskindiseasedetector.scanSkin.ResultScreen
import madproject.deepaks3533898.aiskindiseasedetector.scanSkin.ScanScreen
import madproject.deepaks3533898.aiskindiseasedetector.scanSkin.ScanViewModel
import madproject.deepaks3533898.aiskindiseasedetector.ui.theme.AISkinDiseaseDetectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AISkinDiseaseDetectorTheme {
                AppNavigationMain()
            }
        }
    }
}


@Composable
fun AppNavigationMain() {

    val navController = rememberNavController()
    val context = LocalContext.current

    val sharedViewModel: ScanViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {


        composable(Screen.ScanDisease.route) {

            ScanScreen(
                onAnalyzeClick = {
                    navController.navigate(Screen.Result.route)
                },
                viewModel = sharedViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Result.route) {
            ResultScreen(viewModel = sharedViewModel, onBackClick = {
                navController.popBackStack()
            })
        }

        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigate = {
                    if (PatientData.getUserLoginStatus(context)) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            SignInScreen(
                navController
            )
        }

        composable(Screen.Register.route) {
            SignUpScreen(
                navController
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Profile.route)
        {
            ProfileScreen(navController, PatientData.getEmail(context)!!)
        }

        composable(Screen.ScanHistory.route)
        {
            HistoryScreen(
                viewModel = sharedViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.EduArticles.route)
        {
            EducationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                navController,
                sharedViewModel
            )
        }

        composable(Screen.FullArticle.route)
        {
            FullArticleScreen(
                article = sharedViewModel.selectedArticle!!,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.NearbyHospitals.route)
        {
            ClinicMapScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AboutUs.route)
        {
            AboutUsScreen(navController)
        }


    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object AboutUs : Screen("about_us")

    object ScanDisease : Screen("scan_disease")
    object Result : Screen("result")

    object ScanHistory : Screen("scan_history")
    object EduArticles : Screen("articles")
    object FullArticle : Screen("full_article")
    object NearbyHospitals : Screen("nearby_hospitals")

}


@Composable
fun SplashScreen(onNavigate: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(3000)
        onNavigate()
    }

    SplashScreenDesign()


}


@Composable
fun SplashScreenDesign() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.p4)),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = colorResource(id = R.color.p4))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "AI Skin Disease Detector App!",
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.p3),
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "",
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.bodyMedium,

                )


            Spacer(modifier = Modifier.height(38.dp))

            Image(
                modifier = Modifier.size(200.dp, 200.dp),
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "",
            )



            Spacer(modifier = Modifier.weight(1f))

        }



        Text(
            text = "By",
            color = colorResource(id = R.color.black),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 4.dp, end = 12.dp)
                .align(Alignment.End),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Deepak",
            color = colorResource(id = R.color.black),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 4.dp, end = 12.dp)
                .align(Alignment.End)
        )


    }

}

