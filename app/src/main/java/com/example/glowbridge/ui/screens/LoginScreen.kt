package com.example.glowbridge.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.glowbridge.data.repository.AuthRepository
import com.example.glowbridge.viewmodel.AuthViewModel
import com.example.glowbridge.viewmodel.AuthState
import com.example.glowbridge.viewmodel.AuthViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
@Composable
fun LoginScreen(
    repository: AuthRepository,
    onLoginSuccess: (String) -> Unit
) {
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        SubmitButton(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Email and password cannot be empty"
                } else {
                    errorMessage = null
                    authViewModel.refreshAuthToken()
                    authViewModel.login(email, password)
                }
            }
        )

        when (authState) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Success -> {
                Text("Logged in successfully!")
                LaunchedEffect(Unit) {
                    val userId = (authState as AuthState.Success).userId
                    createUserInFirestore(userId) // Ensure user collection exists
                    onLoginSuccess(userId)
                }
            }
            is AuthState.Error -> Text("Error: ${(authState as AuthState.Error).message}", color = Color.Red)
            is AuthState.LoggedOut -> Text("Logged out")
            else -> {}
        }
    }
}

fun createUserInFirestore(userId: String) {
    val userRef = Firebase.firestore.collection("users").document(userId)

    userRef.get().addOnSuccessListener { document ->
        if (!document.exists()) {
            val userData = hashMapOf(
                "max_streak" to 0,
                "current_streak" to 0,
                "last_task_date" to null
            )
            userRef.set(userData)
                .addOnSuccessListener {
                    Log.d("Firestore", "User document created successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed to create user document: ${e.message}")
                }
        } else {
            Log.d("Firestore", "User document already exists")
        }
    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error checking user document: ${e.message}")
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = { onClick()
    }) {
        Text(text = "Proceed")
    }
}