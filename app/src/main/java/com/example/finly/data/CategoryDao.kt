package com.example.finly.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

// CategoryDao.kt
@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE isForIncome = :isForIncome")
    fun getCategoriesByType(isForIncome: Boolean): Flow<List<Category>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)
}