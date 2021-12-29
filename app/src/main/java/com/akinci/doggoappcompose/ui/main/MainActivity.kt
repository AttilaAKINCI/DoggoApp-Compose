package com.akinci.doggoappcompose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.akinci.doggoappcompose.ui.components.NetworkDependentScreen
import com.akinci.doggoappcompose.ui.feaute.dashboard.DashboardScreenBody
import com.akinci.doggoappcompose.ui.feaute.detail.DetailScreenBody
import com.akinci.doggoappcompose.ui.feaute.splash.SplashScreenBody
import com.akinci.doggoappcompose.ui.main.navigation.Navigation
import com.akinci.doggoappcompose.ui.main.util.DoggoAppState
import com.akinci.doggoappcompose.ui.main.util.rememberDoggoAppState
import com.akinci.doggoappcompose.ui.theme.DoggoAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoggoApp()
        }
    }
}

@Composable
fun DoggoApp(
    appState: DoggoAppState = rememberDoggoAppState()
){
    DoggoAppComposeTheme {
        //  val navController = rememberNavController()
        //  val backstackEntry = navController.currentBackStackEntryAsState()
        //  val currentScreen = MainNavigation.fromRoute(backstackEntry.value?.destination?.route)

        Scaffold(
            // TODO fill later
        ) { innerPadding ->
            MainNavHost(appState, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun MainNavHost(
    appState: DoggoAppState,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = appState.navController,
        startDestination = Navigation.Splash.route,
        modifier = modifier
    ){

        composable(route = Navigation.Splash.route){
            SplashScreenBody(onClick = { appState.navigate(Navigation.Dashboard, it) })
        }
        composable(route = Navigation.Dashboard.route){
            /** For a trial Dashboard Screen is marked as "Network Dependent Screen" (NDS) **/
            NetworkDependentScreen(retryAction = { appState.navigateBack() }) {
                DashboardScreenBody(onClick = { appState.navigate(Navigation.Detail, it) })
            }
        }
        composable(route = Navigation.Detail.route){
            DetailScreenBody(onClick = { appState.navigateBack() })
        }
    }
}