package com.example.glowbridge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.glowbridge.data.repository.StreakTaskRepository

class StreakTaskViewModelFactory(private val repository: StreakTaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreakTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StreakTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
