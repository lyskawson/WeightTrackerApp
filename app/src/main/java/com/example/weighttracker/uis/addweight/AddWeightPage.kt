package com.example.weighttracker.uis.addweight




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weighttracker.customcomposables.WeightAppBar
import com.example.weighttracker.utilities.formatDate
import kotlinx.coroutines.launch

@Composable
fun AddWeightPage(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddWeightViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            WeightAppBar(
                title = "Add Event",
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        WeightForm(
            uiState = viewModel.addWeightUiState,
            onEventValueChange = viewModel::updateUiState,
            onSaveClick = {coroutineScope.launch{viewModel.saveEvent()}},
            modifier = modifier.padding(innerPadding)
        )

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightForm(
    uiState: AddWeightUiState,
    onEventValueChange: (AddWeightDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier

) {
    var openDatePickerDialog by remember { mutableStateOf(false)}
    val datePickerState = rememberDatePickerState()
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextInputFields(
            addWeightDetails = uiState.addWeightDetails,
            onEventValueChange = onEventValueChange
        )
        DatePickerUi(
            shouldOpenDialog = openDatePickerDialog,
            state = datePickerState,
            onDismissRequest = { openDatePickerDialog = false },
            onClickConfirmButton = {
                datePickerState.selectedDateMillis?.let{
                    onEventValueChange(uiState.addWeightDetails.copy(weightDate = formatDate(it)!!))
                }
                openDatePickerDialog = false
            },
            onClickCancelButton = {
                openDatePickerDialog = false
            },
        )
        DatePickerStateButtonUi(
            state = datePickerState,
            onSelectDateButtonClicked = {openDatePickerDialog = true},
        )

        Button(
            onClick = onSaveClick,
            enabled = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ){
            Text(text = "Save")
        }



    }

}

@Composable
fun TextInputFields(
    addWeightDetails: AddWeightDetails,
    onEventValueChange: (AddWeightDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = addWeightDetails.weight,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done),
            onValueChange = {onEventValueChange(addWeightDetails.copy(weight = it))},
            label = {Text(text = "Weight")},
            modifier = modifier.fillMaxWidth().padding(8.dp),
        )
    }
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
    modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        ElevatedButton(
            onClick = onSelectDateButtonClicked
        ) {
            Text(text = "Select Date")
        }
        Text(text = formatDate(state.selectedDateMillis) ?: "No date selected")
    }


}


@Preview(showBackground = true)
@Composable
private fun EventFormPreview() {
    WeightForm(
        uiState = AddWeightUiState(),
        onEventValueChange = {},
        onSaveClick = {}
    )

}
