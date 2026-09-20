package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {
    @Query("SELECT * FROM savings_goals ORDER BY year DESC, month DESC")
    fun getAllGoals(): Flow<List<SavingsGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoal)

    @Query("UPDATE savings_goals SET currentAmount = currentAmount + :amount WHERE id = :goalId")
    suspend fun updateGoalProgress(goalId: Int, amount: Double)

    @Insert
    suspend fun insertLog(log: SavingsLog)

    @Transaction
    suspend fun addSavings(goalId: Int, amount: Double) {
        insertLog(SavingsLog(goalId = goalId, amount = amount))
        updateGoalProgress(goalId, amount)
    }

    @Query("SELECT * FROM savings_logs WHERE goalId = :goalId ORDER BY date DESC")
    fun getLogsForGoal(goalId: Int): Flow<List<SavingsLog>>

    @Query("DELETE FROM savings_goals WHERE id = :id")
    suspend fun deleteGoal(id: Int)
}
