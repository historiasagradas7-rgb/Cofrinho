package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SavingsGoal
import com.example.data.SavingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.map
import java.util.Calendar

class SavingsViewModel(private val repository: SavingsRepository) : ViewModel() {

    data class MonthlySummary(
        val totalSaved: Double,
        val totalTarget: Double,
        val remaining: Double,
        val progress: Float
    )

    val goals: StateFlow<List<SavingsGoal>> = repository.allGoals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val monthlySummary: StateFlow<MonthlySummary?> = goals.map { goalList ->
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        
        val currentGoals = goalList.filter { it.month == currentMonth && it.year == currentYear }
        if (currentGoals.isEmpty()) return@map null
        
        val totalSaved = currentGoals.sumOf { it.currentAmount }
        val totalTarget = currentGoals.sumOf { it.targetAmount }
        val remaining = (totalTarget - totalSaved).coerceAtLeast(0.0)
        val progress = if (totalTarget > 0) (totalSaved / totalTarget).toFloat() else 0f
        
        MonthlySummary(totalSaved, totalTarget, remaining, progress)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun addGoal(name: String, targetAmount: Double, month: Int, year: Int) {
        viewModelScope.launch {
            repository.insertGoal(
                SavingsGoal(
                    name = name,
                    targetAmount = targetAmount,
                    month = month,
                    year = year
                )
            )
        }
    }

    fun addSavings(goalId: Int, amount: Double) {
        viewModelScope.launch {
            repository.addSavings(goalId, amount)
        }
    }

    fun deleteGoal(goalId: Int) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
        }
    }
}
