package com.example.weighttracker.uis.addweight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddWeightViewModel(): ViewModel(){

    var addWeightUiState by mutableStateOf(AddWeightUiState())

    fun updateUiState(addWeightDetails: AddWeightDetails) {
        addWeightUiState = AddWeightUiState(addWeightDetails = addWeightDetails, isEntryValid = validateInput(addWeightDetails))
    }

    private fun validateInput(weightDetails : AddWeightDetails = addWeightUiState.addWeightDetails): Boolean {
        return with(weightDetails) {
            weight.isNotBlank() && weightDate.isNotBlank()
        }
    }

    suspend fun saveEvent(){
        if(validateInput()){

        }
    }


}