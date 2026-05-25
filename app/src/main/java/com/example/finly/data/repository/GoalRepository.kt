package com.example.finly.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.finly.data.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun getAllGoals(): Flow<List<Goal>>

    suspend fun insert(goal: Goal)

    suspend fun update(goal: Goal)

    suspend fun delete(goal: Goal)
}