package com.akinci.doggoappcompose.ui.main.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.akinci.doggoappcompose.ui.main.navigation.Navigation

private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED

@Composable
fun rememberDoggoAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    DoggoAppState(navController)
}

class DoggoAppState(
    val navController: NavHostController
) {
    fun navigateBack() { navController.popBackStack() }

    fun navigate(navigationRoute: Navigation, from: NavBackStackEntry){
        if (from.lifecycleIsResumed()) {
            // In order to discard duplicated navigation events, we check the Lifecycle
            // val encodedUri = Uri.encode(episodeUri)
            // navController.navigate(Screen.Player.createRoute(encodedUri))
            navController.navigate(navigationRoute.route)
        }
    }
}