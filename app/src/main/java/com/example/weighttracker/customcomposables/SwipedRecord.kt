package com.example.weighttracker.customcomposables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SwipedRecord(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit()
                    return@rememberSwipeToDismissBoxState false // Reset, don't complete dismiss
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    showDeleteDialog = true // Show confirmation
                    return@rememberSwipeToDismissBoxState false // Reset, wait for dialog
                }
                SwipeToDismissBoxValue.Settled -> {
                    return@rememberSwipeToDismissBoxState false
                }
            }
        },
        positionalThreshold = { totalDistance -> totalDistance * 0.5f }
    )

    if (showDeleteDialog) {
        AlertDialog(
            title = { Text("Delete Record") },
            text = { Text("Are you sure you want to delete this Record?") },
            onDismissRequest = {
                showDeleteDialog = false
                coroutineScope.launch { dismissState.reset() } // Reset swipe state
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            dismissState.dismiss(SwipeToDismissBoxValue.EndToStart)
                            onDelete()
                            showDeleteDialog = false
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            dismissState.dismiss(SwipeToDismissBoxValue.EndToStart)
                            delay(50L)
                            onDelete()
                            showDeleteDialog = false
                        }
                    }



                ) {
                    Text("Cancel")
                }
            }
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color by animateColorAsState(
                targetValue = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Color(0xFF1E88E5) // Blue for Edit
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFE53935) // Red for Delete
                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                },
                label = "Dismiss Background Color"
            )
            val alignment = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                SwipeToDismissBoxValue.Settled -> Alignment.Center // Or decide based on last direction
            }
            val icon = when (direction) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Filled.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                SwipeToDismissBoxValue.Settled -> null
            }

            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 1f else 1.3f,
                label = "Dismiss Icon Scale"
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                if (icon != null) {
                    Icon(
                        icon,
                        contentDescription = when(direction) {
                            SwipeToDismissBoxValue.StartToEnd -> "Edit Action"
                            SwipeToDismissBoxValue.EndToStart -> "Delete Action"
                            else -> null
                        },
                        modifier = Modifier.scale(scale),
                        tint = Color.White
                    )
                }
            }
        },
        content = content
    )
}