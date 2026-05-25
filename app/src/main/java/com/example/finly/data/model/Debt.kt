package com.example.finly.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val totalAmount: Double,
    val paidAmount: Double = 0.0,
    val dueDate: Long? = null,

    )
