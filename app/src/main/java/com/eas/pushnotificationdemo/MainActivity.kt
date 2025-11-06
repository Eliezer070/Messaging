package com.eas.pushnotificationdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eas.pushnotificationdemo.screens.UsersApp
import com.eas.pushnotificationdemo.ui.theme.PushNotificationDemoTheme
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            // Intervalo mínimo de obtención para desarrollo (puedes aumentarlo para producción)
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        // Establece los valores predeterminados desde un archivo XML si lo deseas
        // remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        setContent {
            // Estado para controlar la visibilidad de la nueva funcionalidad
            var showSpecialFeature by remember { mutableStateOf(false) }

            // Obtiene los valores de Remote Config
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // El valor se actualiza en el estado de Compose
                        // Asegúrate de que "show_special_feature_text" coincida con la clave en Firebase Console
                        showSpecialFeature = remoteConfig.getBoolean("show_special_feature_text")
                        Log.d(
                            "RemoteConfig",
                            "Fetch y activate exitoso, show_special_feature_text: $showSpecialFeature"
                        )
                    } else {
                        Log.e("RemoteConfig", "Fetch fallido", task.exception)
                    }
                }

            PushNotificationDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val token = task.result
                            //storeTokenInFirestore(userId, token)
                            Log.d("FCM token", token)
                        } else {
                            Log.e("FCM", "Failed to fetch token", task.exception)
                        }
                    }

                    // Usamos el estado para decidir si mostrar el componente
                    MainContent(
                        showSpecialFeature = showSpecialFeature,
                        innerPadding = innerPadding // Pasa el padding al contenido
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(showSpecialFeature: Boolean, innerPadding: PaddingValues, modifier: Modifier = Modifier) {
    // Aplica el padding del Scaffold y centra el contenido
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding), // Aplica el padding aquí
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // El componente principal de tu app
        UsersApp(showSpecialFeature)

    }
}
