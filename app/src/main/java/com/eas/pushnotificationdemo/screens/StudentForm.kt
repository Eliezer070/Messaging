// En: app/src/main/java/com/eas/pushnotificationdemo/screens/StudentForm.kt
package com.eas.pushnotificationdemo.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// Data class para representar los datos de un alumno
data class Student(
    val name: String = "",
    val controlNumber: String = "",
    val semester: String = ""
)

@Composable
fun StudentForm() {
    // --- Estados para el formulario de registro ---
    var name by remember { mutableStateOf("") }
    var controlNumber by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    // --- Estados para la funcionalidad de búsqueda ---
    var searchControlNumber by remember { mutableStateOf("") }
    var foundStudent by remember { mutableStateOf<Student?>(null) }
    var searchMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = Firebase.firestore

    // --- SNAPSHOT LISTENER ---
    // Variable para mantener la referencia al listener y poder detenerlo
    val snapshotListener = remember { mutableStateOf<ListenerRegistration?>(null) }

    // DisposableEffect se encarga de limpiar el listener cuando el Composable se va
    DisposableEffect(Unit) {
        onDispose {
            // Detiene el listener cuando la vista se destruye para evitar fugas de memoria
            snapshotListener.value?.remove()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Para permitir scroll
    ) {
        // --- SECCIÓN DE REGISTRO ---
        Text("Registro de Alumno", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        // ... (El resto del formulario de registro se mantiene igual)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = controlNumber,
            onValueChange = { controlNumber = it },
            label = { Text("Número de Control") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semestre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val student = hashMapOf(
                    "name" to name,
                    "controlNumber" to controlNumber,
                    "semester" to semester
                )
                db.collection("alumnos")
                    .add(student)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Alumno registrado con éxito.", Toast.LENGTH_SHORT).show()
                        name = ""
                        controlNumber = ""
                        semester = ""
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error al registrar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            },
            enabled = name.isNotBlank() && controlNumber.isNotBlank() && semester.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Alumno")
        }


        // --- DIVISOR Y SECCIÓN DE BÚSQUEDA ---
        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))

        Text("Búsqueda de Alumno (en tiempo real)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchControlNumber,
            onValueChange = { searchControlNumber = it },
            label = { Text("Buscar por Número de Control") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Primero, detiene cualquier listener anterior
                snapshotListener.value?.remove()
                foundStudent = null
                searchMessage = ""

                // Registra el nuevo listener
                snapshotListener.value = db.collection("alumnos")
                    .whereEqualTo("controlNumber", searchControlNumber)
                    .addSnapshotListener { querySnapshot, error ->
                        // Si hay un error con el listener
                        if (error != null) {
                            searchMessage = "Error al escuchar cambios: ${error.message}"
                            return@addSnapshotListener
                        }

                        // Si la consulta está vacía
                        if (querySnapshot == null || querySnapshot.isEmpty) {
                            foundStudent = null
                            searchMessage = "No se encontró ningún alumno con ese número de control."
                        } else {
                            // Convierte el primer documento encontrado al objeto Student
                            foundStudent = querySnapshot.documents.first().toObject(Student::class.java)
                            searchMessage = "" // Limpia mensajes de error
                        }
                    }
            },
            enabled = searchControlNumber.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar y Escuchar Cambios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ÁREA PARA MOSTRAR RESULTADOS ---
        if (foundStudent != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Datos del Alumno", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    // El !! es seguro aquí porque ya comprobamos que no es nulo
                    Text("Nombre: ${foundStudent!!.name}")
                    Text("No. Control: ${foundStudent!!.controlNumber}")
                    Text("Semestre: ${foundStudent!!.semester}")
                }
            }
        } else if (searchMessage.isNotEmpty()) {
            Text(searchMessage)
        }
    }
}
