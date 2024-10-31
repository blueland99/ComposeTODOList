package com.blueland.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.blueland.todo.navigation.Route
import com.blueland.todo.ui.MainScreen
import com.blueland.todo.ui.SettingScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Route.Main.route) {
        composable(route = Route.Main.route) {
            MainScreen(navController = navController)
        }

        composable(route = Route.Setting.route) {
            SettingScreen(navController = navController)
        }
    }
}

