package com.example.weighttracker.data.entities


data class WeightRecord(
    val id: Long = 0,
    val weight: Double = 0.0,
    val weightDate: String,
    val completed : Boolean = false,

    )