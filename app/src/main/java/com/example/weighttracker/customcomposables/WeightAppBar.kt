package com.example.weighttracker.customcomposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = if (canNavigateBack) navigateUp else onMenuClick
            ) {
                Icon(
                    imageVector = if (canNavigateBack)
                        Icons.AutoMirrored.Filled.ArrowBack
                    else
                        Icons.Filled.Menu,
                    contentDescription = if (canNavigateBack) "Back" else "Menu"
                )
            }
        }
    )
}