package com.example.weighttracker.uis.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weighttracker.customcomposables.SwipedRecord
import com.example.weighttracker.customcomposables.WeightAppBar
import com.example.weighttracker.data.entities.WeightRecord
import com.example.weighttracker.uis.addweight.AddWeightBottomSheetContent
import com.example.weighttracker.uis.addweight.AddWeightViewModel
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    viewModelHome : HomeViewModel = hiltViewModel()
) {
    val viewModel: AddWeightViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    var showSheet by remember { mutableStateOf(false) }
    val uiState by viewModelHome.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            WeightAppBar(
                title = "Weight Tracker",
                canNavigateBack = false,
                onMenuClick = openDrawer
            )
        },
        floatingActionButton = {
            if (!showSheet) {
                ExtendedFloatingActionButton(
                    text = { Text("Add Weight") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                    onClick = {
                        viewModel.resetAddWeightState()
                        showSheet = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()

        ) {




            if (showSheet) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(configuration.screenHeightDp.dp * 0.5f)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color.White)
                        .zIndex(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding()
                            .padding(horizontal = 24.dp)

                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),


                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            Text("Add a record", style = MaterialTheme.typography.titleMedium)
                            IconButton(onClick = { showSheet = false }) {
                                Icon(Icons.Filled.Close, contentDescription = "Close")
                            }
                        }

                        AddWeightBottomSheetContent(
                            uiState = viewModel.addWeightUiState,
                            onEventValueChange = viewModel::updateUiState,
                            onCancel = { showSheet = false },
                            onSave = {
                                coroutineScope.launch {
                                    if (viewModel.addWeightUiState.addWeightDetails.id != 0L) {
                                        viewModel.updateRecord()
                                    } else {
                                        viewModel.saveRecord()
                                    }
                                    showSheet = false
                                }
                            }
                        )
                    }
                }
            }

            if(uiState.records.isEmpty()){
                EmptyList(
                    message = "No events foun\n Add an event to get started",
                    modifier = modifier .padding(innerPadding)
                )
                return@Scaffold
            }

            WeightList(
                records = uiState.records,
                modifier = modifier.padding(innerPadding),
                onDelete = {
                    coroutineScope.launch {
                        viewModelHome.deleteRecord(it)
                    }
                },
                onEdit = {
                    viewModel.startEditing(it)
                    showSheet = true;



                }
            )






        }
    }
}

@Composable
fun EmptyList(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ){
        Text(message, textAlign = TextAlign.Center)
    }
}

@Composable
fun WeightList(
    records: List<WeightRecord>,
    modifier: Modifier = Modifier,
    onDelete: (WeightRecord) -> Unit,
    onEdit: (WeightRecord) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(records) { record ->
            WeightRecordView(
                record = record,
                onEdit = {onEdit(record)},
                onDelete = {onDelete(record)}
            )
        }


    }
}

@Composable
fun WeightRecordView(
    record: WeightRecord,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    ) {

    SwipedRecord(
        onDelete = onDelete,
        onEdit = onEdit
    ) {
        ListItem(
            tonalElevation = 10.dp,
            headlineContent = {
                Text(record.weight.toString())
            },
            trailingContent = {
                Text(record.weightDate, style = MaterialTheme.typography.bodyLarge)
            },

        )
    }




}





