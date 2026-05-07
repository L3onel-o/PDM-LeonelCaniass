package com.example.lab2

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF4F6F8)
                ) {
                    NombresApp()
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NombresApp() {
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
