package com.example.finly.data.repository

import com.example.finly.data.dao.CategoryDao
import com.example.finly.data.model.Category
import kotlinx.coroutines.flow.Flow

class OfflineCategRepository (private val categoryDao: CategoryDao) : CategoryRepository {

    override fun getCategoriesByType(isForIncome: Boolean): Flow<List<Category>> = categoryDao.getCategoriesByType(isForIncome)

    override fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    override suspend fun insert(category: Category) = categoryDao.insert(category)

    override suspend fun delete(category: Category) = categoryDao.delete(category)
}