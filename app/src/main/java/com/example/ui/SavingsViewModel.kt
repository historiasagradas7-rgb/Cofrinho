package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SavingsGoal
import com.example.data.SavingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavingsViewModel(private val repository: SavingsRepository) : ViewModel() {

    val goals: StateFlow<List<SavingsGoal>> = repository.allGoals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
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
