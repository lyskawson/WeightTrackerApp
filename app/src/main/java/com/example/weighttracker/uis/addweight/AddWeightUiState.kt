package com.example.weighttracker.uis.addweight

import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.utilities.formatTimestampToString

data class AddWeightDetails(
    val id: Long = 0,
    val weight: String = "",
    val weightDate: String = "",

    )

data class AddWeightUiState(
    val addWeightDetails: AddWeightDetails = AddWeightDetails(
        weight = "",
        weightDate = formatTimestampToString(System.currentTimeMillis())),
    val isEntryValid: Boolean = false,
)


//extension function to convert AddEventDetails to ShoppingEvent
fun AddWeightDetails.toWeightRecord(parsedTimestamp: Long): WeightRecord = WeightRecord(
    id = id,
    // Add safe conversion for weight, defaulting to 0.0 if invalid
    weight = weight.toDoubleOrNull() ?: 0.0,
    weightDate = parsedTimestamp, // Use the Long timestamp passed in
)