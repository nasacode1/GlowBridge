package com.example.glowbridge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var username by remember { mutableStateOf("") } // State variable
    var password by remember { mutableStateOf("") }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp), // Adds padding to avoid overlap
        horizontalAlignment = Alignment.CenterHorizontally, // Centers horizontally
        verticalArrangement = Arrangement.Center // Centers vertically
    ){
        TextField(
            value =  username, // Binds the state to the TextField
            placeholder = { Text(text = "Enter your username") },
            onValueChange = { newText -> username = newText   }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            placeholder = { Text(text = "Enter password") },
            onValueChange = {newText -> password = newText},
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(10.dp))
        SubmitButton(
            onClick = {
                onLoginSuccess(username)
            }
        )

    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Submit")
    }
}