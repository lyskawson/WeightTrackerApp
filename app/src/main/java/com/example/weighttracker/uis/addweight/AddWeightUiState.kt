package com.example.weighttracker.uis.addweight

import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.utilities.formatDate

data class AddWeightDetails(
    val id: Long = 0,
    val weight: String = "",
    val weightDate: String = "",
    val completed: Boolean = false,
    )

data class AddWeightUiState(
    val addWeightDetails: AddWeightDetails = defaultAddWeightDetails(),
    val isEntryValid: Boolean = false,
)

fun defaultAddWeightDetails(): AddWeightDetails {
    return AddWeightDetails(
        weightDate = formatDate(System.currentTimeMillis(), pattern = "yyyy-MM-dd") ?: ""
    )
}

//extension function to convert AddEventDetails to ShoppingEvent
fun AddWeightDetails.toShoppingEvent(): WeightRecord = WeightRecord(
    id = id,
    weight = weight.toDouble(),
    weightDate = weightDate,
    completed = completed


)