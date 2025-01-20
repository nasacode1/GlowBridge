package com.example.glowbridge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomePage(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(text = "Hey, welcome to Glow Bridge.")
        Spacer(modifier = Modifier.height(40.dp))
        CardMinimalExample()
    }

}

@Composable
fun CardMinimalExample() {
    Card(
        modifier = Modifier.size(width =300.dp, height = 100.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        SubmitButton()
    }
}

@Composable
fun SubmitButton() {
    Button(onClick = { }) {
        Text(text = "Click to book an appointment with nutritionists")
    }

}