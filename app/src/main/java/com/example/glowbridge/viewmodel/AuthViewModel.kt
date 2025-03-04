package com.example.glowbridge.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowbridge.data.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedOut : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            refreshAuthToken()

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    if (user != null) {
                        _authState.value = AuthState.Success(user.uid)
                        createUserDocumentIfNotExists(user)
                    } else {
                        _authState.value = AuthState.Error("User is null after login.")
                    }
                }
                .addOnFailureListener { e ->
                    _authState.value = AuthState.Error("Login failed: ${e.message}")
                }
        }
    }

    fun refreshAuthToken() {
        val user = auth.currentUser
        user?.getIdToken(true)
            ?.addOnSuccessListener { result ->
                val newToken = result.token
                Log.d("Auth", "Token refreshed: $newToken")
            }
            ?.addOnFailureListener { e ->
                Log.e("Auth", "Failed to refresh token: ${e.message}")
            }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.LoggedOut
    }

    private fun createUserDocumentIfNotExists(user: FirebaseUser) {
        val userRef = firestore.collection("users").document(user.uid)

        userRef.get()
            .addOnSuccessListener { document ->
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
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error checking user document: ${e.message}")
            }
    }

    fun reauthenticateUser(email: String, password: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(email, password)

        user?.reauthenticate(credential)
            ?.addOnSuccessListener {
                Log.d("Auth", "Reauthenticated successfully")
            }
            ?.addOnFailureListener { e ->
                Log.e("Auth", "Reauthentication failed: ${e.message}")
            }
    }
}