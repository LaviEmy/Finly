package com.example.finly.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.finly.data.model.Debt
import kotlinx.coroutines.flow.Flow

interface DebtRepository {

    fun getAllDebts(): Flow<List<Debt>>

    suspend fun insert(debt: Debt)

    suspend fun update(debt: Debt)

    suspend fun delete(debt: Debt)
}