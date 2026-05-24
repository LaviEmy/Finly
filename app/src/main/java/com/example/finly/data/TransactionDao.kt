package com.example.finly.data

import androidx.constraintlayout.helper.widget.Flow
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update

interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate")
    fun getTransactionsByPeriod(startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT categoryId, SUM(amount) as total FROM transactions WHERE type = :type GROUP BY categoryId")
    fun getTotalsByCategory(type: String): Flow<List<CategoryTotal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)
}

data class CategoryTotal(
    val categoryId: Int,
    val total: Double
)
