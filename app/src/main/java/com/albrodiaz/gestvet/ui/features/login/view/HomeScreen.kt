package com.albrodiaz.gestvet.ui.features.login.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.albrodiaz.gestvet.ui.features.login.view.navigation.LoginNavHost

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    LoginNavHost(navController = navController)
}