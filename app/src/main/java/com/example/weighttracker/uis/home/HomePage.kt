package com.example.weighttracker.uis.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weighttracker.customcomposables.WeightAppBar

@Composable
fun HomePage(
    navigateToAddWeight: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            WeightAppBar(
                title = "Weight Tracker",
                canNavigateBack = false,
                onMenuClick = openDrawer
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddWeight
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }

    ) { innerPadding ->
        Text(
            text = "Home Page",
            modifier = modifier.padding(innerPadding)
        )

    }

}