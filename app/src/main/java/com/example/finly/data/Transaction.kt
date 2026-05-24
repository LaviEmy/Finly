package com.example.finly.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Int,
    val date: Long = System.currentTimeMillis(),
    val note: String = ""
)
