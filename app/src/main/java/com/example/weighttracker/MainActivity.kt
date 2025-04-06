package com.example.weighttracker


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weighttracker.customcomposables.AppDrawer
import com.example.weighttracker.destinations.AddWeightRoute
import com.example.weighttracker.destinations.BMIRoute
import com.example.weighttracker.destinations.HomeRoute
import com.example.weighttracker.destinations.KcalRoute
import com.example.weighttracker.ui.theme.WeightTrackerTheme
import com.example.weighttracker.uis.home.HomePage
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeightApp()
        }
    }
}

@Composable
fun WeightApp(modifier: Modifier = Modifier) {
    WeightTrackerTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Surface(
                    modifier = Modifier.fillMaxHeight()
                        .width(LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                        .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                    shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                    tonalElevation = 4.dp,
                ) {
                    AppDrawer { destination ->
                        scope.launch { drawerState.close() }
                        navController.navigate(destination) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                }
            }
        ) {


            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier
            ) {
                composable<HomeRoute> {
                    HomePage(
                        //navigateToAddWeight = { navController.navigate(AddWeightRoute) },
                        modifier = modifier,
                        openDrawer = { scope.launch { drawerState.open() } },
                    )
                }

                composable<AddWeightRoute> {
//                        AddWeightPage(
//                            modifier = modifier,
//                            navigateBack = { navController.popBackStack() },
//                            navigateUp = { navController.navigateUp() }
//                        )
                }

                composable<BMIRoute> {
                    //BMICalculatorPage() // ← define this composable
                }

                composable<KcalRoute> {
                    //KcalCalculatorPage() // ← define this composable
                }
            }

        }
    }
}