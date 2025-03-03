package com.example.glowbridge.ui.screens

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
        Spacer(modifier = Modifier.height(16.dp))

        SubmitButton(
            onClick = {
                authViewModel.login(email, password)
            }
        )

        when (authState) {
            is AuthState.Loading -> CircularProgressIndicator()
            is AuthState.Success -> {
                Text("Logged in successfully!")
                LaunchedEffect(Unit) {
                    onLoginSuccess((authState as AuthState.Success).userId)
                }
            }
            is AuthState.Error -> Text("Error: ${(authState as AuthState.Error).message}")
            is AuthState.LoggedOut -> Text("Logged out")
            else -> {}
        }
    }
}

fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    Firebase.auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener { authResult ->
            val user = authResult.user
            user?.let {
                val userId = it.uid
                val userRef = Firebase.firestore.collection("users").document(userId)

                userRef.get().addOnSuccessListener { document ->
                    if (!document.exists()) {
                        val userData = hashMapOf(
                            "max_streak" to 0,  // Default values
                            "current_streak" to 0,
                            "last_task_date" to null
                        )
                        userRef.set(userData)
                    }
                }

                onSuccess()
            }
        }
        .addOnFailureListener { e ->
            onFailure(e.localizedMessage ?: "Login failed")
        }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
    Button(onClick = { onClick()
    }) {
        Text(text = "Proceed")
    }
}

