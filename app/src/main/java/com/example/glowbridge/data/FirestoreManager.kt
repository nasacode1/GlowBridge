package com.example.glowbridge.data

import com.google.firebase.firestore.FirebaseFirestore
object FirestoreManager {
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }



}