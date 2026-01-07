package com.devildevil.cristin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CristinUI(onStartListening: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cristin AI", style = MaterialTheme.typography.headlineMedium)
        Text("Cristin is running...", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onStartListening) {
            Text("Start Listening")
        }
    }
}
