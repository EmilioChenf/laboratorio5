package com.example.laboratorio5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laboratorio5.ui.theme.Laboratorio5Theme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val food = intent.getStringExtra("food") ?: "—"
        val price = intent.getStringExtra("price") ?: "—"

        setContent {
            Laboratorio5Theme {
                Surface {
                    Column(Modifier.padding(20.dp)) {
                        Text(text = "Detalles", style = MaterialTheme.typography.headlineMedium)
                        Text(
                            text = "Tipo de comida: $food",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "¿Qué tan caro es?: $price",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Convención: Barato = Q, Normal = QQ, Caro = QQQ",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
