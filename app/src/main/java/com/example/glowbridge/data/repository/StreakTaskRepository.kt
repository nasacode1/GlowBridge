package com.example.glowbridge.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class StreakTaskRepository {
    private val db = Firebase.firestore
    private val tasksCollection = db.collection("tasks")

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
