package com.example.finly.data

import kotlinx.coroutines.flow.Flow

class OfflineTransRepository (private val transactionDao: TransactionDao) : TransactionsRepository{

    override fun getTransactionsByType(type: String): Flow<List<Transaction>> = transactionDao.getTransactionsByType(type)

    override fun getTransactionsByPeriod(startDate: Long, endDate: Long): Flow<List<Transaction>> = transactionDao.getTransactionsByPeriod(startDate, endDate)

    override fun getTotalsByCategory(type: String): Flow<List<CategoryTotal>> = transactionDao.getTotalsByCategory(type)

    override suspend fun insert(transaction: Transaction) = transactionDao.insert(transaction)

    override suspend fun delete(transaction: Transaction) = transactionDao.delete(transaction)

    override suspend fun update(transaction: Transaction) = transactionDao.update(transaction)
}