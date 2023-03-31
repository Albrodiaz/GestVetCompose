package com.albrodiaz.gestvet.ui.features.login.view.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.albrodiaz.gestvet.ui.features.home.views.mainscreen.MainScreen
import com.albrodiaz.gestvet.GestVetRoutes
import com.albrodiaz.gestvet.ui.features.login.view.LoginInputScreen
import com.albrodiaz.gestvet.ui.features.login.view.RegisterScreen

@Composable
fun LoginNavHost(navController: NavController, showSnack: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = GestVetRoutes.LoginScreen.route
        ) {
            composable(GestVetRoutes.LoginScreen.route) {
                LoginInputScreen(
                    showError = { message -> showSnack(message) },
                    navigateRegister = { navController.navigate(GestVetRoutes.RegisterScreen.route) },
                    navigateHome = {
                        navController.navigate(GestVetRoutes.MainScreen.route) {
                            popUpTo(GestVetRoutes.LoginScreen.route) { inclusive = true }
                        }
                    })
            }
            composable(GestVetRoutes.RegisterScreen.route) {
                RegisterScreen(
                    showResult = { message -> showSnack(message) },
                    navigateBack = { navController.popBackStack(route = GestVetRoutes.LoginScreen.route, inclusive = false) }
                ) }
            composable(GestVetRoutes.MainScreen.route) { MainScreen() }
        }
    }
}