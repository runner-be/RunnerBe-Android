package com.applemango.presentation.ui.screen.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.applemango.presentation.ui.screen.compose.login.LoginView

@Composable
fun SignNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginView(navController)
        }
    }
}