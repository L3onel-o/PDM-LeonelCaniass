package com.example.labo3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.labo3.ui.theme.Labo3Theme
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labo3Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = RutaHome) {
                    composable<RutaHome> {
                        HomeScreen(navController)
                    }
                    composable<RutaNombres> {
                        NombresApp(navController)
                    }
                    composable<RutaSensores> {
                        SensorScreen(navController)
                    }
                }
            }


        }
    }
}

@Composable
fun NombresApp(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var listaNombres by remember { mutableStateOf(listOf<String>()) }

    val verdePrincipal = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF43A047)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Lista de Nombres",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = verdePrincipal
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Nombre",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = verdePrincipal,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Ingresa un nombre", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = verdePrincipal,
                unfocusedBorderColor = Color(0xFFB0BEC5)
            )
        )


        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank()) {
                    listaNombres = listaNombres + nombre.trim()
                    nombre = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = verdePrincipal),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Listado de nombres y\nposición en la lista",
                fontSize = 14.sp,
                color = Color(0xFF546E7A),
                fontWeight = FontWeight.Medium
            )

            OutlinedButton(
                onClick = { listaNombres = emptyList() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = verdePrincipal)
            ) {
                Text("Limpiar", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .border(2.dp, verdeClaro, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(listaNombres) { index, nombreItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = nombreItem, fontSize = 16.sp, color = Color(0xFF263238))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = verdePrincipal,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    if (index < listaNombres.lastIndex) {
                        HorizontalDivider(color = Color(0xFFECEFF1), thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun useSensor(sensorType: Int): List<Float> {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val sensor = sensorManager.getDefaultSensor(sensorType) ?: return emptyList()
    var sensorValues by remember { mutableStateOf(listOf(0f, 0f, 0f)) }

    DisposableEffect(sensorType) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.let {
                    sensorValues = it.toList()
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
    return sensorValues
}

@Composable
fun SensorScreen(navController: NavController) {
    val gyroscopeValues = useSensor(Sensor.TYPE_GYROSCOPE)
    val verdePrincipal = Color(0xFF2E7D32)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Giroscopio",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = verdePrincipal
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (gyroscopeValues.isNotEmpty()) {
                Text(text = "Eje X: ${gyroscopeValues[0]}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Eje Y: ${gyroscopeValues[1]}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Eje Z: ${gyroscopeValues[2]}", fontSize = 18.sp)
            } else {
                Text(text = "Tu dispositivo no cuenta con giroscopio.", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = verdePrincipal)
            ) {
                Text("Regresar al Menú Principal")
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val verdePrincipal = Color(0xFF2E7D32)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Laboratorio 3",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = verdePrincipal
        )
        Text(
            text = "Menú Principal",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate(RutaNombres) },
            colors = ButtonDefaults.buttonColors(containerColor = verdePrincipal),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Ver Lista de Nombres", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate(RutaSensores) },
            colors = ButtonDefaults.buttonColors(containerColor = verdePrincipal),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Ver Sensor (Giroscopio)", fontSize = 16.sp)
        }
    }
}

@Serializable
object RutaHome

@Serializable
object RutaNombres

@Serializable
object RutaSensores
