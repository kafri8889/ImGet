package com.anafthdev.imget.ui.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.imget.data.ImGetScreen
import com.anafthdev.imget.ui.home.HomeScreen
import com.anafthdev.imget.ui.home.HomeViewModel

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ImGetScreen.Home.name
    ) {
        composable(ImGetScreen.Home.name) { backEntry ->
            val viewModel = hiltViewModel<HomeViewModel>(backEntry)

            HomeScreen(
                viewModel = viewModel
            )
        }
    }

}
