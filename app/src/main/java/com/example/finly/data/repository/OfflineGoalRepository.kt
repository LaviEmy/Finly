package com.example.finly.data.repository

import com.example.finly.data.dao.GoalDao
import com.example.finly.data.model.Goal
import kotlinx.coroutines.flow.Flow

class OfflineGoalRepository(private val goalDao : GoalDao) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> = goalDao.getAllGoals()

    override suspend fun insert(goal: Goal) = goalDao.insert(goal)

    override suspend fun update(goal: Goal) = goalDao.update(goal)

    override suspend fun delete(goal: Goal) = goalDao.delete(goal)
}