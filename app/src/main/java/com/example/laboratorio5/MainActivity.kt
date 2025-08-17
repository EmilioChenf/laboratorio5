package com.example.laboratorio5

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.laboratorio5.ui.theme.Laboratorio5Theme
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

/* ======= EDITA SOLO ESTAS CONSTANTES ======= */
private const val FULL_NAME = "TU NOMBRE COMPLETO"
private val BIRTHDAY_MONTH = Month.SEPTEMBER
private const val BIRTHDAY_DAY = 30

// Restaurante favorito
private const val RESTO_NAME = "Frico Grill Cayalá"
private const val RESTO_ADDRESS = "Paseo Cayalá, Guatemala"
private const val OPEN_HOURS = "12:00PM 10:00PM"
private const val FOOD_TYPE = "Rápida / Grill"
private const val PRICE_LEVEL = "QQ"

// Play Store del botón “Descargar”
private const val TARGET_PACKAGE = "com.whatsapp"

// Consulta para Google Maps (nombre + lugar)
private const val MAPS_QUERY = "Frisco Grill Cayalá, Paseo Cayalá"
/* =========================================== */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Laboratorio5Theme { MainScreen() } }
    }
}

@Composable
private fun MainScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Actualización disponible",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { openPlayStoreOrWeb(context, TARGET_PACKAGE) }) {
                        Text("Descargar")
                    }
                }
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val (weekday, dateText) = remember { birthdayForThisYear() }
            Text(
                weekday,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                dateText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = { /* opcional */ }, modifier = Modifier.align(Alignment.End)) {
                Text("Terminar jornada")
            }

            Spacer(Modifier.height(16.dp))
            RestaurantCard(
                onStart = { Toast.makeText(context, FULL_NAME, Toast.LENGTH_SHORT).show() },
                onDetails = {
                    val i = Intent(context, DetailsActivity::class.java)
                        .putExtra("food", FOOD_TYPE)
                        .putExtra("price", PRICE_LEVEL)
                    context.startActivity(i)
                },
                onDirections = { openMapsWithQuery(context, MAPS_QUERY) }
            )
        }
    }
}

@Composable
private fun RestaurantCard(
    onStart: () -> Unit,
    onDetails: () -> Unit,
    onDirections: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text(RESTO_NAME, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(RESTO_ADDRESS, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Text(OPEN_HOURS, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onStart, modifier = Modifier.widthIn(min = 160.dp)) {
                        Text("Iniciar")
                    }
                    TextButton(onClick = onDetails) { Text("Detalles") }
                }
            }
            IconButton(onClick = onDirections, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                Icon(Icons.Filled.Directions, contentDescription = "Directions")
            }
        }
    }
}

/* ================= Helpers ================= */

private fun birthdayForThisYear(): Pair<String, String> {
    val locale = Locale("es", "ES")
    val now = LocalDate.now()
    val date = LocalDate.of(now.year, BIRTHDAY_MONTH, BIRTHDAY_DAY)
    val weekday = date.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    val day = date.dayOfMonth.toString()
    val month = date.month.getDisplayName(TextStyle.FULL, locale)
    val dateText = "$day de $month"
    return weekday to dateText
}

private fun openPlayStoreOrWeb(context: Context, packageName: String) {
    try {
        val market = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            .apply {
                setPackage("com.android.vending")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        context.startActivity(market)
        return
    } catch (_: ActivityNotFoundException) { /* fallback */ }

    val web = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(web)
}

private fun openMapsWithQuery(context: Context, query: String) {
    val mapsIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:0,0?q=${Uri.encode(query)}")
    ).setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(mapsIntent)
    } catch (_: ActivityNotFoundException) {
        val web = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(query)}")
        context.startActivity(Intent(Intent.ACTION_VIEW, web))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMain() {
    Laboratorio5Theme { MainScreen() }
}
