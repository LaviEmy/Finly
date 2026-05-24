package com.example.finly.data.repository

import com.example.finly.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getCategoriesByType(isForIncome: Boolean): Flow<List<Category>>

    fun getAllCategories(): Flow<List<Category>>

    suspend fun insert(category: Category)

    suspend fun delete(category: Category)
}