package com.example.finly.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finly.data.model.TransactionType

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