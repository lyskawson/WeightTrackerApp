package com.example.weighttracker.uis.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.data.repos.WeightRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
private val weightRecordRepository : WeightRecordRepository
) : ViewModel(){
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow() // to make it immutable

    init{
        viewModelScope.launch {
            weightRecordRepository.getRecords().collect{ records ->
                _homeUiState.update{
                    it.copy(records = records)
                }
            }
        }
    }
    fun deleteRecord(record: WeightRecord) {
        viewModelScope.launch {
            weightRecordRepository.delete(record)
        }
    }



}