package com.example.finly.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finly.data.model.Category
import kotlinx.coroutines.flow.Flow

// CategoryDao.kt
@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE isForIncome = :isForIncome")
    fun getCategoriesByType(isForIncome: Boolean): Flow<List<Category>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)
}