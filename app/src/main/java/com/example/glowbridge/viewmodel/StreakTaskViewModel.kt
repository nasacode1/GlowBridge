package com.example.glowbridge.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowbridge.data.repository.StreakTaskRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class StreakTaskViewModel(private val repository: StreakTaskRepository,
                          private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _task = MutableLiveData<String>()
    val task: LiveData<String> = _task

    private val _currentStreak = MutableLiveData<Int>()
    val currentStreak: LiveData<Int> = _currentStreak

    private val _maxStreak = MutableLiveData<Int>()
    val maxStreak: LiveData<Int> = _maxStreak

    private val _isStarExpanded = MutableLiveData<Boolean>()
    val isStarExpanded: LiveData<Boolean> = _isStarExpanded

    init {
        loadTask()
//        loadStreakData()
        checkIfNewDay()
    }

    private fun loadTask() {
        val savedTask = sharedPreferences.getString("task_of_the_day", null)
        val lastUpdated = sharedPreferences.getLong("task_timestamp", 0)

        if (savedTask != null && isSameDay(lastUpdated)) {
            _task.value = savedTask?: "Not available"
        } else {
            fetchNewTask()
        }
    }

    fun fetchNewTask() {
        viewModelScope.launch {
            val newTask = repository.getRandomTask()
            newTask?.let {
                _task.value = it
                sharedPreferences.edit()
                    .putString("task_of_the_day", it)
                    .putLong("task_timestamp", System.currentTimeMillis())
                    .apply()
            }
        }
    }

//    private fun loadStreakData() {
//        _currentStreak.value = sharedPreferences.getInt("current_streak", 0)
//        _isStarExpanded.value = sharedPreferences.getBoolean("is_star_expanded", false)
//
//        repository.getMaxStreak { maxStreakValue ->
//            _maxStreak.value = maxStreakValue
//        }
//    }

    private fun checkIfNewDay() {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        if (!isSameDay(lastUpdated)) {
            sharedPreferences.edit().putBoolean("is_star_expanded", false).apply()
//            updateStreak()
        }
    }

//    private fun updateStreak() {
//        val current = _currentStreak.value ?: 0
//        val max = _maxStreak.value ?: 0
//        val newStreak = if (wasTaskCompletedYesterday()) current + 1 else 0
//
//        _currentStreak.value = newStreak
//        sharedPreferences.edit()
//            .putInt("current_streak", newStreak)
//            .putLong("streak_last_updated", System.currentTimeMillis())
//            .apply()
//
//        if (newStreak > max) {
//            _maxStreak.value = newStreak
//            repository.updateMaxStreak(newStreak) // ✅ Update Firestore
//        }
//    }

    private fun wasTaskCompletedYesterday(): Boolean {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = cal.timeInMillis
        return isSameDay(lastUpdated, yesterday)
    }

    private fun isSameDay(timestamp: Long, compareTo: Long = System.currentTimeMillis()): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance().apply { timeInMillis = compareTo }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun markTaskCompleted() {
        sharedPreferences.edit().putBoolean("is_star_expanded", true).apply()
        _isStarExpanded.value = true
    }
}
