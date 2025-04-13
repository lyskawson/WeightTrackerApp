package com.example.weighttracker.uis.home

import com.example.weighttracker.data.entities.WeightRecord


data class HomeUiState(
    val records: List<WeightRecord> = emptyList(),
)