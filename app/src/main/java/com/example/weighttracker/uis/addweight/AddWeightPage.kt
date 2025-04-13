package com.example.weighttracker.uis.addweight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weighttracker.utilities.formatDate
import com.example.weighttracker.utilities.parseDateToMillis


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWeightBottomSheetContent(
    uiState: AddWeightUiState,
    onEventValueChange: (AddWeightDetails) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    val initialDateMillis = parseDateToMillis(uiState.addWeightDetails.weightDate)


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )
    var openDatePickerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        OutlinedTextField(
            value = uiState.addWeightDetails.weight,
            onValueChange = {
                onEventValueChange(uiState.addWeightDetails.copy(weight = it))
            },
            label = { Text(text ="Weight", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DatePickerStateButtonUi(
            state = datePickerState,
            onSelectDateButtonClicked = { openDatePickerDialog = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = onSave,
                enabled = uiState.isEntryValid,
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Date picker modal
    DatePickerUi(
        shouldOpenDialog = openDatePickerDialog,
        state = datePickerState,
        onDismissRequest = { openDatePickerDialog = false },
        onClickConfirmButton = {
            datePickerState.selectedDateMillis?.let {
                onEventValueChange(uiState.addWeightDetails.copy(weightDate = formatDate(it)!!))
            }
            openDatePickerDialog = false
        },
        onClickCancelButton = { openDatePickerDialog = false }
    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUi(
    shouldOpenDialog: Boolean,
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onClickConfirmButton: () -> Unit,
    onClickCancelButton: () -> Unit,
    modifier: Modifier = Modifier) {

    if(shouldOpenDialog) {
        val confirmEnabled by remember{
            derivedStateOf{state.selectedDateMillis != null}
        }


        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    enabled = confirmEnabled,
                    onClick = onClickConfirmButton
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onClickCancelButton
                ) {
                    Text("CANCEL")
                }
            },

            ){
            DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerStateButtonUi(
    state: DatePickerState,
    onSelectDateButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = formatDate(state.selectedDateMillis) ?: "No date selected"

    // Button containing the date and calendar icon
    OutlinedButton(
        onClick = onSelectDateButtonClicked,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge, // Same rounded corners as other fields
        contentPadding = PaddingValues(16.dp)
    ) {
        // Show the calendar icon and the selected date text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
            Text(text = formattedDate, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

