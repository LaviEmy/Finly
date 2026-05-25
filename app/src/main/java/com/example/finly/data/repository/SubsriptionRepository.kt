package com.example.finly.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.finly.data.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubsriptionRepository {

    fun getAllSubscriptions(): Flow<List<Subscription>>

    suspend fun insert(subscription: Subscription)

    suspend fun update(subscription: Subscription)

    suspend fun delete(subscription: Subscription)
}