package com.example.weighttracker.uis.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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
    viewModelHome: HomeViewModel = hiltViewModel()
) {
    val viewModel: AddWeightViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    var showSheet by remember { mutableStateOf(false) }
    val uiState by viewModelHome.homeUiState.collectAsState()

    val screenHeight = configuration.screenHeightDp.dp
    val listState = rememberLazyListState()

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
                        showSheet = true
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedVisibility(
                visible = showSheet,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1f),

                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 300),
                    initialOffsetY = { fullHeight -> fullHeight }
                ),

                exit = slideOutVertically(
                    animationSpec = tween(durationMillis = 300),
                    targetOffsetY = { fullHeight -> fullHeight }
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.5f)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .navigationBarsPadding() // Add padding for navigation bar
                            .padding(horizontal = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Add a record",
                                // Use M3 Typography
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = {
                                showSheet = false // Trigger animation OUT
                            }) {
                                Icon(Icons.Filled.Close, contentDescription = "Close")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp)) // Add some space


                        AddWeightBottomSheetContent(
                            uiState = viewModel.addWeightUiState,
                            onEventValueChange = viewModel::updateUiState,
                            onCancel = {
                                showSheet = false
                            },
                            onSave = {
                                coroutineScope.launch {
                                    if (viewModel.addWeightUiState.addWeightDetails.id != 0L)
                                    {
                                        viewModel.updateRecord()
                                    }
                                    else
                                    {
                                        viewModel.saveRecord()
                                        listState.animateScrollToItem(0)
                                    }
                                }
                                showSheet = false
                            }
                        )

                    }
                }
            }

            if (uiState.records.isEmpty() && !showSheet)
            {
                EmptyList(
                    message = "No records found.\n Add a record to get started.",
                    modifier = modifier
                        .padding(innerPadding)
                        .align(Alignment.Center) // Center empty message
                )

            }
            else if (!uiState.records.isEmpty())
            {
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
                        showSheet = true
                    },
                    listState = listState
                )
            }
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
    onEdit: (WeightRecord) -> Unit,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(
            items = records,
            key = {record -> record.id}) { record ->
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
            leadingContent = {
                Text(record.weightDate, style = MaterialTheme.typography.bodyLarge)
            },
            headlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Weight icon",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("${record.weight} kg" , style = MaterialTheme.typography.titleMedium)

                }

            },
            trailingContent = {
                Text("+0.5 kg" , style = MaterialTheme.typography.titleSmall)
            },

            )
    }
}