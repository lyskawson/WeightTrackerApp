package com.example.weighttracker.uis.addweight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.data.repos.WeightRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddWeightViewModel @Inject constructor(private val weightRecordRepository: WeightRecordRepository): ViewModel(){

    var addWeightUiState by mutableStateOf(AddWeightUiState())

    fun updateUiState(addWeightDetails: AddWeightDetails) {
        addWeightUiState = AddWeightUiState(addWeightDetails = addWeightDetails,
            isEntryValid = validateInput(addWeightDetails))
    }

    private fun validateInput(weightDetails : AddWeightDetails = addWeightUiState.addWeightDetails): Boolean {
        return with(weightDetails) {
            weight.isNotBlank() && weightDate.isNotBlank()
        }
    }

    fun resetAddWeightState() {
        addWeightUiState = AddWeightUiState()
    }

    suspend fun saveRecord(){
        if(validateInput()) {
            weightRecordRepository.insert(addWeightUiState.addWeightDetails.toWeightRecord())
        }
    }

    suspend fun updateRecord() {
        if (validateInput()) {
            weightRecordRepository.update(addWeightUiState.addWeightDetails.toWeightRecord())
        }
    }

    fun startEditing(record: WeightRecord) {
        addWeightUiState = AddWeightUiState(
            addWeightDetails = AddWeightDetails(
                id = record.id,
                weight = record.weight.toString(),
                weightDate = record.weightDate
            ),
            isEntryValid = true
        )
    }


}