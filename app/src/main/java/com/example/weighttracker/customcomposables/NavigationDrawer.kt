package com.example.weighttracker.customcomposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weighttracker.destinations.AddWeightRoute
import com.example.weighttracker.destinations.BMIRoute
import com.example.weighttracker.destinations.HomeRoute
import com.example.weighttracker.destinations.KcalRoute


@Composable
fun AppDrawer(
    onDestinationClicked: (Any) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Menu", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem("Home") { onDestinationClicked(HomeRoute) }
        DrawerItem("Add Weight") { onDestinationClicked(AddWeightRoute) }
        DrawerItem("BMI Calculator") { onDestinationClicked(BMIRoute) }
        DrawerItem("Kcal Calculator") { onDestinationClicked(KcalRoute) }
    }
}

@Composable
fun DrawerItem(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(12.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}