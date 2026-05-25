package com.example.finly.data.repository

import com.example.finly.data.dao.DebtDao
import com.example.finly.data.model.Debt
import kotlinx.coroutines.flow.Flow

class OfflineDebtRepository(private val debtDao : DebtDao) : DebtRepository {

    override fun getAllDebts(): Flow<List<Debt>> = debtDao.getAllDebts()

    override suspend fun insert(debt: Debt) = debtDao.insert(debt)

    override suspend fun update(debt: Debt) = debtDao.update(debt)

    override suspend fun delete(debt: Debt) = debtDao.delete(debt)
}