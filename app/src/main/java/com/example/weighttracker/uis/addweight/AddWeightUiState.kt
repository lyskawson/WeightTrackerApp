package com.example.weighttracker.uis.addweight

import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.utilities.formatDate

data class AddWeightDetails(
    val id: Long = 0,
    val weight: String = "",
    val weightDate: String = "",

    )

data class AddWeightUiState(
    val addWeightDetails: AddWeightDetails = AddWeightDetails(
        weight = "",
        weightDate = formatDate(System.currentTimeMillis()) ?: ""),
    val isEntryValid: Boolean = false,
)


//extension function to convert AddEventDetails to ShoppingEvent
fun AddWeightDetails.toWeightRecord(): WeightRecord = WeightRecord(
    id = id,
    weight = weight.toDouble(),
    weightDate = weightDate,



)