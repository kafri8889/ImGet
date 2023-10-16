package com.anafthdev.imget.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.imget.data.ImGetScreen
import com.anafthdev.imget.ui.home.HomeScreen

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ImGetScreen.Home.name
    ) {
        composable(ImGetScreen.Home.name) {
            HomeScreen()
        }
    }

}
