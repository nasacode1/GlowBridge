package com.example.glowbridge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowbridge.data.repository.StreakTaskRepository
import kotlinx.coroutines.launch

class StreakTaskViewModel(private val repository: StreakTaskRepository) : ViewModel() {
    private val _task = MutableLiveData<String>()
    val task: LiveData<String> = _task

    fun fetchRandomTask() {
        viewModelScope.launch {
            val fetchedTask = repository.getRandomTask()
            _task.value = fetchedTask ?: "No task found"
        }
    }
}
