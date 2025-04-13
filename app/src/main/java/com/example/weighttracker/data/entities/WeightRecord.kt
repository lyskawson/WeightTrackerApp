package com.example.weighttracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_records")
data class WeightRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weight: Double = 0.0,
    @ColumnInfo(name = "weight_date") val weightDate: String,

    )