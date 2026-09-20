package com.example.data

import kotlinx.coroutines.flow.Flow

class SavingsRepository(private val savingsDao: SavingsDao) {
    val allGoals: Flow<List<SavingsGoal>> = savingsDao.getAllGoals()

    suspend fun insertGoal(goal: SavingsGoal) = savingsDao.insertGoal(goal)

    suspend fun addSavings(goalId: Int, amount: Double) = savingsDao.addSavings(goalId, amount)

    fun getLogsForGoal(goalId: Int): Flow<List<SavingsLog>> = savingsDao.getLogsForGoal(goalId)

    suspend fun deleteGoal(id: Int) = savingsDao.deleteGoal(id)
}
