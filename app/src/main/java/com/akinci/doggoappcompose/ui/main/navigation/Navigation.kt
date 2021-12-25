package com.akinci.doggoappcompose.ui.main.navigation

/**
 * For parametered navigation define create route function.
 * **/

sealed class Navigation(val route: String){
    object Splash: Navigation("splash")
    object Dashboard: Navigation("dashboard")
    object Detail: Navigation("detail")
}