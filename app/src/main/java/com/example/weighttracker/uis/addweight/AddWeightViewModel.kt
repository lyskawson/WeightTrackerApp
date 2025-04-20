package com.example.weighttracker.uis.addweight

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.data.repos.WeightRecordRepository
import com.example.weighttracker.utilities.formatTimestampToString
import com.example.weighttracker.utilities.parseStringToTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWeightViewModel @Inject constructor(private val weightRecordRepository: WeightRecordRepository): ViewModel(){

    var addWeightUiState by mutableStateOf(AddWeightUiState())
        private set // Make setter private to control updates via methods

    fun updateUiState(addWeightDetails: AddWeightDetails) {
        addWeightUiState = addWeightUiState.copy( // Use copy for state updates
            addWeightDetails = addWeightDetails,
            isEntryValid = validateInput(addWeightDetails)
            // Reset date error when user types
            // dateError = if (parseStringToTimestamp(addWeightDetails.weightDate) == null && addWeightDetails.weightDate.isNotBlank()) "Invalid date format" else null
        )
    }

    private fun validateInput(weightDetails: AddWeightDetails = addWeightUiState.addWeightDetails): Boolean {
        return with(weightDetails) {
            // --- CHANGE HERE: Validate weight and if date string can be parsed ---
            val isWeightValid = weight.isNotBlank() && weight.toDoubleOrNull() != null && weight.toDouble() > 0
            // Check if date string is blank OR if it parses successfully
            val isDateValid = weightDate.isBlank() || parseStringToTimestamp(weightDate) != null
            // For entry to be valid overall, both must be valid AND date must not be blank
            isWeightValid && weightDate.isNotBlank() && parseStringToTimestamp(weightDate) != null
        }
    }

    fun resetAddWeightState() {
        // Re-initialize with default (today's date formatted)
        addWeightUiState = AddWeightUiState()
    }

    fun saveRecord() { // Remove suspend, launch coroutine inside
        viewModelScope.launch {
            // --- CHANGE HERE: Parse date string BEFORE creating WeightRecord ---
            val parsedTimestamp = parseStringToTimestamp(addWeightUiState.addWeightDetails.weightDate)

            // Re-validate just before saving, including the parsing result
            if (validateInput() && parsedTimestamp != null) {
                // Pass the parsed Long to the modified extension function
                weightRecordRepository.insert(addWeightUiState.addWeightDetails.toWeightRecord(parsedTimestamp))
                // Optionally: Reset state or navigate back after successful save
                // resetAddWeightState()
            } else {
                // Handle validation/parsing error (e.g., update UI state with error message)
                println("Save Error: Invalid input or date format.")
                // addWeightUiState = addWeightUiState.copy(dateError = "Invalid date format")
            }
        }
    }

    fun updateRecord() { // Remove suspend, launch coroutine inside
        viewModelScope.launch {
            // --- CHANGE HERE: Parse date string BEFORE creating WeightRecord ---
            val parsedTimestamp = parseStringToTimestamp(addWeightUiState.addWeightDetails.weightDate)

            // Re-validate just before saving, including the parsing result
            if (validateInput() && parsedTimestamp != null) {
                // Pass the parsed Long to the modified extension function
                weightRecordRepository.update(addWeightUiState.addWeightDetails.toWeightRecord(parsedTimestamp))
                // Optionally: Reset state or navigate back after successful update
                // resetAddWeightState()
            } else {
                // Handle validation/parsing error
                println("Update Error: Invalid input or date format.")
                // addWeightUiState = addWeightUiState.copy(dateError = "Invalid date format")
            }
        }
    }


    fun startEditing(record: WeightRecord) {
        Log.d("AddWeightVM_startEdit", "[Entry] Received Record ID: ${record.id}, Weight: ${record.weight}, Date(Long): ${record.weightDate}")
        // Log state BEFORE modification
        Log.d("AddWeightVM_startEdit", "[Before Update] Current addWeightUiState.addWeightDetails: ${addWeightUiState.addWeightDetails}")

        val weightString = record.weight.toString()
        val dateString = formatTimestampToString(record.weightDate) // Use your helper
        val detailsForEdit = AddWeightDetails(
            id = record.id,
            weight = weightString,
            weightDate = dateString
        )
        // --- CHANGE HERE: Create a completely new state object ---
        addWeightUiState = AddWeightUiState(
            addWeightDetails = detailsForEdit,
            isEntryValid = true // Assume valid when loading for edit
        )
        Log.d("AddWeightVM_startEdit", "[After Update] New addWeightUiState.addWeightDetails: ${addWeightUiState.addWeightDetails}")

    }


}