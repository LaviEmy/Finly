package com.example.finly.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameResId: String,
    val isForIncome: Boolean,
    val isDefault: Boolean = false
)
