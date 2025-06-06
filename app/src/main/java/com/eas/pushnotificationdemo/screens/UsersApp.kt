
package com.eas.pushnotificationdemo.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UsersApp( modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    val mContext = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Main card Content for Register
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Add verticalScroll modifier here
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            ) {

                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    text = "Register",
                    fontSize = 25.sp
                )

                // Name Field
                OutlinedTextField(
                    value = name, // Add a variable for name
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    label = { Text("Mobile Number") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (name.isEmpty()) {
                                Toast.makeText(mContext, "Name is Empty", Toast.LENGTH_SHORT).show()
                            } else if (mobileNumber.isEmpty()) {
                                Toast.makeText(mContext, "Mobile No. is Empty", Toast.LENGTH_SHORT)
                                    .show()
                            } else if (email.isEmpty()) {
                                Toast.makeText(mContext, "Email is Empty", Toast.LENGTH_SHORT)
                                    .show()
                            } else {

                                name = ""
                                email = ""
                                mobileNumber = ""
                                keyboardController?.hide()
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Registar")
                    }
                }
            }
        }
        val gradientColors = listOf(Color.Cyan, Color.Magenta, Color.Yellow /*...*/)
        //Columna para ver los resultados de la consulta
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the list of friends
            LazyColumn(
                modifier = Modifier.weight(.7F),
                verticalArrangement = Arrangement.Center
            ) {
                /*items(usersList) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        ) {
                            Text(modifier= Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left, color = Color.White, fontSize = 20.sp,
                                text = "Name: " + user.name)
                            Text(modifier= Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left, color = Color.White, fontSize = 20.sp, fontStyle = FontStyle.Italic,
                                text = "Email: " + user.email)
                            Text(modifier= Modifier.fillMaxWidth(),
                                text = "Phone: " + user.phone,
                                textAlign = TextAlign.Left, color = Color.Gray, fontSize = 20.sp, fontStyle = FontStyle.Italic,
                                style = androidx.compose.ui.text.TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                )
                            )
                        }
                    }
                }*/
            }
        }
    }
}