package com.anafthdev.imget.ui.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anafthdev.imget.data.ImGetDestination
import com.anafthdev.imget.ui.home.HomeScreen
import com.anafthdev.imget.ui.home.HomeViewModel
import com.anafthdev.imget.ui.setting.SettingScreen
import com.anafthdev.imget.ui.setting.SettingViewModel

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ImGetDestination.Home.name
    ) {
        composable(ImGetDestination.Home.name) { backEntry ->
            val viewModel = hiltViewModel<HomeViewModel>(backEntry)

            HomeScreen(
                viewModel = viewModel,
                navigateTo = { dest ->
                    navController.navigate(dest.name)
                }
            )
        }

        composable(ImGetDestination.Setting.name) { backEntry ->
            val viewModel = hiltViewModel<SettingViewModel>(backEntry)

            SettingScreen(
                viewModel = viewModel,
                onNavigationIconClicked = navController::popBackStack
            )
        }
    }

}
