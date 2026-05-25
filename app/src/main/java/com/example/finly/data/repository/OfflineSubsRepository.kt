package com.example.finly.data.repository

import com.example.finly.data.dao.SubscriptionDao
import com.example.finly.data.model.Subscription
import kotlinx.coroutines.flow.Flow

class OfflineSubsRepository (private val subscriptionDao: SubscriptionDao) : SubscriptionRepository{

    override fun getAllSubscriptions(): Flow<List<Subscription>> = subscriptionDao.getAllSubscriptions()

    override suspend fun insert(subscription: Subscription) = subscriptionDao.insert(subscription)

    override suspend fun update(subscription: Subscription) = subscriptionDao.update(subscription)

    override suspend fun delete(subscription: Subscription) = subscriptionDao.delete(subscription)

}