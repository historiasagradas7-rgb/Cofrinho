package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "savings_goals")
data class SavingsGoal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val month: Int,
    val year: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "savings_logs")
data class SavingsLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalId: Int,
    val amount: Double,
    val date: Long = System.currentTimeMillis()
)
