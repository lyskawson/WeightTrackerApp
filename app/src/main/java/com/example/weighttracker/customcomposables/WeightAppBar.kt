package com.example.weighttracker.customcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


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
        title = {Text(title)},
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack){
                IconButton(
                    onClick = navigateUp

                ){
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)

                }
            }
        },
        actions = {
            if (!canNavigateBack) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        },

    )


}

