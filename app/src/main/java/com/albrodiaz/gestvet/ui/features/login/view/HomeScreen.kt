package com.albrodiaz.gestvet.ui.features.login.view

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.albrodiaz.gestvet.ui.features.login.view.navigation.LoginNavHost
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        LoginNavHost(navController = navController) {
            scope.launch {
                snackbarHostState.showSnackbar(message = it)
            }
        }
    }
}