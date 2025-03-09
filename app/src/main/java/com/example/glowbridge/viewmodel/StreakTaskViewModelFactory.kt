package com.example.glowbridge.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.glowbridge.data.repository.StreakTaskRepository

class StreakTaskViewModelFactory(private val repository: StreakTaskRepository, private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreakTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StreakTaskViewModel(repository, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
