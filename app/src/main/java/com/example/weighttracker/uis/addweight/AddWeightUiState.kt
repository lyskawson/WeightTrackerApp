package com.example.weighttracker.uis.addweight

import com.example.weighttracker.data.entities.WeightRecord

data class AddWeightDetails(
    val id: Long = 0,
    val weight: String = "",
    val weightDate: String = "",
    val completed: Boolean = false,
    )

data class AddWeightUiState(
    val addWeightDetails: AddWeightDetails = AddWeightDetails(),
    val isEntryValid: Boolean = false,
)

//extension function to convert AddEventDetails to ShoppingEvent
fun AddWeightDetails.toShoppingEvent(): WeightRecord = WeightRecord(
    id = id,
    weight = weight.toDouble(),
    weightDate = weightDate,
    completed = completed


)