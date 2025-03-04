package com.example.glowbridge.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.glowbridge.data.repository.StreakTaskRepository
import kotlinx.coroutines.launch
import java.util.Calendar
class StreakTaskViewModel(
    private val repository: StreakTaskRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _task = MutableLiveData<String>()
    val task: LiveData<String> = _task

    private val _currentStreak = MutableLiveData<Int>()
    val currentStreak: LiveData<Int> = _currentStreak

    private val _maxStreak = MutableLiveData<Int>()
    val maxStreak: LiveData<Int> = _maxStreak

    private val _isStarExpanded = MutableLiveData<Boolean>()
    val isStarExpanded: LiveData<Boolean> = _isStarExpanded

    private val _lastTaskDate = MutableLiveData<String?>()
    val lastTaskDate: LiveData<String?> = _lastTaskDate

    init {
        loadTask()
        loadStreakData()
        checkIfNewDay()
    }

    private fun loadTask() {
        val savedTask = sharedPreferences.getString("task_of_the_day", null)
        val lastUpdated = sharedPreferences.getLong("task_timestamp", 0)

        if (savedTask != null && isSameDay(lastUpdated)) {
            _task.value = savedTask ?: "Not available"
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

    fun setStarExpanded(expanded: Boolean) {
        _isStarExpanded.value = expanded
        sharedPreferences.edit().putBoolean("is_star_expanded", expanded).apply()
    }

    private fun loadStreakData() {
        _currentStreak.value = sharedPreferences.getInt("current_streak", 0)
        _isStarExpanded.value = sharedPreferences.getBoolean("is_star_expanded", false)

        _maxStreak.value = sharedPreferences.getInt("max_streak", 0)

        repository.getMaxStreak { maxStreakValue ->
            _maxStreak.postValue(maxStreakValue)
            sharedPreferences.edit().putInt("max_streak", maxStreakValue).apply()
        }

        val lastUpdatedDate = sharedPreferences.getLong("streak_last_updated", 0)
        if (!isSameDay(lastUpdatedDate, System.currentTimeMillis())) {
            sharedPreferences.edit().putBoolean("is_star_expanded", false).apply()
            _isStarExpanded.postValue(false)
        }
    }
    private fun isFirstTimeLoggingIn(): Boolean {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        return lastUpdated == 0L
    }


    fun updateStreak() {
        val current = _currentStreak.value ?: 0
        val max = _maxStreak.value ?: 0
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)

        val newStreak = if (wasTaskCompletedYesterday() || isFirstTimeLoggingIn()) {
            current + 1
        } else {
            0
        }

        _currentStreak.postValue(newStreak)
        sharedPreferences.edit()
            .putInt("current_streak", newStreak)
            .putLong("streak_last_updated", System.currentTimeMillis())
            .apply()

        if (newStreak > max) {
            _maxStreak.postValue(newStreak)
            sharedPreferences.edit().putInt("max_streak", newStreak).apply()
            repository.updateMaxStreak(newStreak)
        }
    }


    private fun checkIfNewDay() {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)

        if (!isSameDay(lastUpdated)) {
            sharedPreferences.edit().putBoolean("is_star_expanded", false).apply()
            _isStarExpanded.postValue(false)
            updateStreak()
        }
    }

    private fun wasTaskCompletedYesterday(): Boolean {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return isSameDay(lastUpdated, cal.timeInMillis)
    }

    private fun isSameDay(timestamp: Long, compareTo: Long = System.currentTimeMillis()): Boolean {
        if (timestamp == 0L) return false
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance().apply { timeInMillis = compareTo }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun markTaskCompleted() {
        sharedPreferences.edit().putBoolean("is_star_expanded", true).apply()
        _isStarExpanded.postValue(true)
    }
}
