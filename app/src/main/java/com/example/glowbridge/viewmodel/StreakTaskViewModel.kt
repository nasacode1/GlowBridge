package com.example.glowbridge.viewmodel

import android.content.SharedPreferences
import android.util.Log
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

    val isNewLogin = true
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
        loadStarState()
    }

    private fun loadStarState() {
        val expanded = sharedPreferences.getBoolean("is_star_expanded", false)
        _isStarExpanded.value = expanded
    }

    fun loadTask() {
        val savedTask = sharedPreferences.getString("task_of_the_day", null)
        val lastUpdated = sharedPreferences.getLong("task_timestamp", 0)

        Log.d("StreakDebug", "Loading task - savedTask: $savedTask, lastUpdated: $lastUpdated")

        if (savedTask != null && isSameDay(lastUpdated)) {
            Log.d("StreakDebug", "Task is the same day, using saved task.")
            _task.value = savedTask ?: "Not available"
        } else {
            Log.d("StreakDebug", "Task is not from today, fetching a new task.")
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
        val savedCurrentStreak = sharedPreferences.getInt("current_streak", -1)
        val savedMaxStreak = sharedPreferences.getInt("max_streak", -1)

        if (savedCurrentStreak == -1 || savedMaxStreak == -1) {
            Log.d("StreakDebug", "Fetching streak data from Firestore...")
            repository.loadStreakData { maxStreak, currentStreak, lastTaskDate ->
                _currentStreak.postValue(currentStreak)
                _maxStreak.postValue(maxStreak)

                sharedPreferences.edit()
                    .putInt("current_streak", currentStreak)
                    .putInt("max_streak", maxStreak)
                    .putLong("streak_last_updated", System.currentTimeMillis())
                    .apply()
            }
        } else {
            _currentStreak.postValue(savedCurrentStreak)
            _maxStreak.postValue(savedMaxStreak)
        }
    }

    private fun isFirstTimeLoggingIn(): Boolean {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        return lastUpdated == 0L
    }

    fun updateStreak() {
        val lastUpdated = sharedPreferences.getLong("streak_last_updated", 0)
        Log.d("StreakDebug", "$lastUpdated")
        val lastUpdateDate = Calendar.getInstance().apply { timeInMillis = lastUpdated }
        val today = Calendar.getInstance()

        val completedYesterday = wasTaskCompletedYesterday()
        val isNewLogin = lastUpdated == 0L || lastUpdateDate.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)

        var currentStreak = _currentStreak.value ?: 0
        var maxStreak = _maxStreak.value ?: 0

        Log.d("StreakDebug", "Before update -> Current: $currentStreak, Max: $maxStreak, Last Updated: $lastUpdated")
        Log.d("StreakDebug", "isNewLogin: $isNewLogin, completedYesterday: $completedYesterday")

        if (isNewLogin) {
            if (completedYesterday) {
                currentStreak += 1
            } else {
                currentStreak = 1
            }
        }

        Log.d("StreakDebug", "New Streak after update: $currentStreak")

        _currentStreak.value = currentStreak

        sharedPreferences.edit()
            .putInt("current_streak", currentStreak)
            .putLong("streak_last_updated", System.currentTimeMillis())
            .apply()

        if (currentStreak > maxStreak) {
            maxStreak = currentStreak
            _maxStreak.value = maxStreak
            sharedPreferences.edit().putInt("max_streak", maxStreak).apply()
            repository.updateMaxStreak(maxStreak)
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
        val lastCompletedDateMillis = sharedPreferences.getLong("streak_last_updated", 0)
        if (lastCompletedDateMillis == 0L) return false

        val lastCompletedDate = Calendar.getInstance().apply { timeInMillis = lastCompletedDateMillis }
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

        return lastCompletedDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                lastCompletedDate.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
    }

    private fun isSameDay(timestamp: Long, compareTo: Long = System.currentTimeMillis()): Boolean {
        if (timestamp == 0L) return false
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance().apply { timeInMillis = compareTo }

        val sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)

        Log.d("StreakDebug", "Checking isSameDay -> timestamp: $timestamp, compareTo: $compareTo, sameDay: $sameDay")
        return sameDay
    }

    fun markTaskCompleted() {
        sharedPreferences.edit().putBoolean("is_star_expanded", true).apply()
        _isStarExpanded.postValue(true)
    }
}
