
package com.eas.pushnotificationdemo.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

@Composable
fun UsersApp(showSpecialFeature: Boolean, modifier: Modifier = Modifier) {
    // Get an instance of FirebaseAnalytics
    val firebaseAnalytics = Firebase.analytics
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // El texto que se mostrará/ocultará con Remote Config
        if (showSpecialFeature) {
            Text(text = "¡Funcionalidad especial activada desde Remote Config!")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Log a custom event to Firebase Analytics
                firebaseAnalytics.logEvent("crash_button_clicked") {
                    param("screen_name", "UsersApp")
                    param("enviar_evento", "Evento enviado")
                }

            }
        ) {
            Text("Enviar Evento")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Genera una excepción no controlada para forzar el crash
                throw RuntimeException("¡Este es un crash provocado!")
            }
        ) {
            Text("Provocar Crash")
        }
    }

}