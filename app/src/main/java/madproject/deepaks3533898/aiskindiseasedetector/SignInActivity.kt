package madproject.deepaks3533898.aiskindiseasedetector

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SignInScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var useremail by remember { mutableStateOf("") }
    var userpassword by remember { mutableStateOf("") }

//    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.p4)),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.p4))
                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(100.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Column(modifier = Modifier.clickable {

                }) {

                    Text(
                        text = "Welcome!",
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Sign in to Continue",
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.bodyMedium,

                        )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Ai Skin Disease Detector",
                )

            }

            Spacer(modifier = Modifier.height(82.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white)),
                value = useremail,
                onValueChange = { useremail = it },
                placeholder = { Text("Email") },

                leadingIcon = {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "user",
                    )
                },

                )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white)),
                value = userpassword,
                onValueChange = { userpassword = it },
                placeholder = { Text("Password") },
                leadingIcon = {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_password),
                        contentDescription = "password",
                    )
                },

                )

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                modifier = Modifier
                    .clickable {
                        when {
                            useremail.isEmpty() -> {
//                            Toast.makeText(context, " Please Enter Mail", Toast.LENGTH_SHORT).show()
                            }

                            userpassword.isEmpty() -> {
//                            Toast.makeText(context, " Please Enter Password", Toast.LENGTH_SHORT)
//                                .show()
                            }

                            else -> {


                                onLoginSuccess.invoke()
//                                val personDetails = PersonDetails(
//                                    "",
//                                    useremail,
//                                    "",
//                                    "",
//                                    userpassword
//                                )
//
//                                loginUser(personDetails,context)
                            }

                        }
                    }
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "Ai Skin Disease Detector",
            )

            Spacer(modifier = Modifier.height(12.dp))

        }



        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(
                text = "New to User? ",
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = "Register now",
                color = colorResource(id = R.color.p3),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black),
                modifier = Modifier.clickable {
                    onLoginSuccess.invoke()

//                    context.startActivity(Intent(context, SignUpActivity::class.java))
//                    context.finish()
                }
            )

        }

        Spacer(modifier = Modifier.height(36.dp))
    }

}
//
//fun loginUser(personDetails: PersonDetails, context: Context) {
//
//    val firebaseDatabase = FirebaseDatabase.getInstance()
//    val databaseReference = firebaseDatabase.getReference("PersonDetails").child(personDetails.emailid.replace(".", ","))
//
//    databaseReference.get().addOnCompleteListener { task ->
//        if (task.isSuccessful) {
//            val dbData = task.result?.getValue(PersonDetails::class.java)
//            if (dbData != null) {
//                if (dbData.password == personDetails.password) {
//
//                    WorkoutTrackerData.writeLS(context, true)
//                    WorkoutTrackerData.writeMail(context, dbData.emailid)
//                    WorkoutTrackerData.writeUserName(context, dbData.name)
//
//                    Toast.makeText(context, "Login Sucessfully", Toast.LENGTH_SHORT).show()
//
//                    context.startActivity(Intent(context, WorkoutHomeActivity::class.java))
//                    (context as Activity).finish()
//
//                } else {
//                    Toast.makeText(context, "Seems Incorrect Credentials", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(context, "Your account not found", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(
//                context,
//                "Something went wrong",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//    }
//}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
//    LoginScreen()
}