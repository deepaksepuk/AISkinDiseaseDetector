package madproject.deepaks3533898.aiskindiseasedetector

import android.app.Activity
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase


@Composable
fun SignUpScreen(navController: NavController) {

    var userName by remember { mutableStateOf("") }

    var userAge by remember { mutableStateOf("") }

    var userWeight by remember { mutableStateOf("") }

    var userGender by remember { mutableStateOf("") }

    var useremail by remember { mutableStateOf("") }
    var userpassword by remember { mutableStateOf("") }

    val context = LocalContext.current as Activity

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
        ) {

            Spacer(modifier = Modifier.height(50.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Column(modifier = Modifier.clickable {

                }) {

                    Text(
                        text = "Register!",
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Sign up ",
                        color = colorResource(id = R.color.black),
                        style = MaterialTheme.typography.bodyMedium,

                        )
                }

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier
                        .clickable {


                        }
                        .size(100.dp),
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
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = "email",
                    )
                },

                )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white)),
                value = userName,
                onValueChange = { userName = it },
                placeholder = { Text("UserName") },
                leadingIcon = {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "userName",
                    )
                },

                )
            Spacer(modifier = Modifier.height(12.dp))

            Row {

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .background(color = colorResource(id = R.color.white)),
                    value = userAge,
                    onValueChange = { userAge = it },
                    placeholder = { Text("Age") },
                    leadingIcon = {
                        Image(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_age),
                            contentDescription = "age",
                        )
                    },

                    )

                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .background(color = colorResource(id = R.color.white)),
                    value = userWeight,
                    onValueChange = { userWeight = it },
                    placeholder = { Text("Weight") },
                    leadingIcon = {
                        Image(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_weight),
                            contentDescription = "weight",
                        )
                    },

                    )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(color = colorResource(id = R.color.white))
                    .padding(12.dp)
            ) {

                Text(
                    text = "Select Gender",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(id = R.color.black)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    RadioButton(
                        selected = userGender == "Male",
                        onClick = { userGender = "Male" }
                    )
                    Text("Male")

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = userGender == "Female",
                        onClick = { userGender = "Female" }
                    )
                    Text("Female")


                }
            }


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
                            Toast.makeText(context, " Please Enter Mail", Toast.LENGTH_SHORT).show()
                            }

                            userName.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Name", Toast.LENGTH_SHORT)
                                .show()
                            }

                            userAge.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Age", Toast.LENGTH_SHORT)
                                .show()
                            }

                            userWeight.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Weight", Toast.LENGTH_SHORT)
                                .show()
                            }

                            userGender.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Gender", Toast.LENGTH_SHORT)
                                .show()
                            }

                            userpassword.isEmpty() -> {
                            Toast.makeText(context, " Please Enter Password", Toast.LENGTH_SHORT)
                                .show()
                            }

                            else -> {
                                val personDetails = PersonDetails(
                                    userName,
                                    useremail,
                                    userAge,
                                    userWeight,
                                    userGender,
                                    userpassword
                                )
                                registerUser(personDetails,context,navController)
                            }

                        }

                    }
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "AI Skin Disease Detector",
            )

            Spacer(modifier = Modifier.height(12.dp))

        }


        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Have Account ? ",
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = "Login Now",
                color = colorResource(id = R.color.p3),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black),
                modifier = Modifier.clickable {

                    navController.popBackStack()
                }
            )

            Spacer(modifier = Modifier.height(36.dp))


        }
    }

}

fun registerUser(personDetails: PersonDetails, context: Context,navController: NavController) {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("PatientAccounts")

    databaseReference.child(personDetails.emailid.replace(".", ","))
        .setValue(personDetails)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "You Registered Successfully", Toast.LENGTH_SHORT)
                    .show()

                navController.popBackStack()
            } else {
                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { _ ->
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
}

data class PersonDetails(
    var name: String = "",
    var emailid: String = "",
    var age: String = "",
    var weight: String = "",
    var gender: String = "",
    var password: String = ""
)
