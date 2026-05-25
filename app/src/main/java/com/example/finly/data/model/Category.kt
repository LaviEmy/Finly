package com.example.finly.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameResId: String,
    val isForIncome: Boolean,
    val isDefault: Boolean = false,
    val color: String = "#4CAF50"  // hex
)