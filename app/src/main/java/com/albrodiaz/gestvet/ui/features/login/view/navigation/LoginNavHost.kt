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
import com.albrodiaz.gestvet.ui.features.home.views.navigation.Routes
import com.albrodiaz.gestvet.ui.features.login.view.LoginInputScreen
import com.albrodiaz.gestvet.ui.features.login.view.RegisterScreen

@Composable
fun LoginNavHost(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = Routes.LoginScreen.route
        ) {
            composable(Routes.LoginScreen.route) { LoginInputScreen() }
            composable(Routes.RegisterScreen.route) { RegisterScreen() }
            composable(Routes.MainScreen.route) { MainScreen() }
        }
    }
}