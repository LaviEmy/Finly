package com.example.finly.data.repository

import com.example.finly.data.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {

    fun getAllSubscriptions(): Flow<List<Subscription>>

    suspend fun insert(subscription: Subscription)

    suspend fun update(subscription: Subscription)

    suspend fun delete(subscription: Subscription)
}