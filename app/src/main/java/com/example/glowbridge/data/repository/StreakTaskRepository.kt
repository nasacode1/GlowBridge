package com.example.glowbridge.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class StreakTaskRepository {
    private val db = Firebase.firestore
    private val tasksCollection = db.collection("tasks")
    private val userId = Firebase.auth.currentUser?.uid ?: "default_user"

    fun getMaxStreak(onComplete: (Int) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val maxStreak = document.getLong("max_streak")?.toInt() ?: 0
                onComplete(maxStreak)
            }
            .addOnFailureListener {
                onComplete(0)
            }
    }

    fun updateMaxStreak(newMaxStreak: Int) {
        db.collection("users").document(userId)
            .update("max_streak", newMaxStreak)
            .addOnFailureListener {
                Log.e("Firestore", "Failed to update max streak")
            }
    }

    fun loadStreakData(onComplete: (Int, Int, String?) -> Unit){
        db.collection("users").document(userId).get()
            .addOnSuccessListener {document ->
                if (document.exists()) {
                    val maxStreak = document.getLong("max_streak")?.toInt() ?: 0
                    val currentStreak = document.getLong("current_streak")?.toInt() ?: 0
                    val lastTaskDate = document.getString("last_task_date") ?: ""
                    onComplete(maxStreak, currentStreak, lastTaskDate)
                } else {
                    Log.e("Firestore", "User document does not exist!")
                    onComplete(0, 0, null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to load streak data: ${e.message}")
                onComplete(0, 0, null)
            }
    }

    fun updateStreakData(newMaxStreak: Int, newCurrentStreak: Int, newDate: String) {
        val userRef = db.collection("users").document(userId)
        val updates = mapOf(
            "max_streak" to newMaxStreak,
            "current_streak" to newCurrentStreak,
            "last_task_date" to newDate
        )

        userRef.update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Streak data updated successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to update streak data: ${e.message}")
            }
    }

    suspend fun getRandomTask(): String? {
        return try {
            val documents = tasksCollection.get().await()
            if (!documents.isEmpty) {
                val randomDoc = documents.documents.random()
                randomDoc.getString("task")
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("StreakTaskRepository", "Error fetching task", e)
            null
        }
    }
}

