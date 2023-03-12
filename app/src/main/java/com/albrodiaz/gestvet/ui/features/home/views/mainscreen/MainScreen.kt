package com.albrodiaz.gestvet.ui.features.home.views.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.albrodiaz.gestvet.ui.features.home.models.Routes
import com.albrodiaz.gestvet.ui.features.home.viewmodels.AppointmentViewModel

@Composable
fun MainScreen(
    navController: NavController,
    appointmentViewModel: AppointmentViewModel
) {
    val navStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavState = rememberSaveable { mutableStateOf(true) }

    when (navStackEntry?.destination?.route) {
        Routes.Appointment.route, Routes.Search.route, Routes.Client.route -> {
            bottomNavState.value = true
        }
        else -> bottomNavState.value = false
    }

    Scaffold(
        bottomBar = { if (!bottomNavState.value) return@Scaffold else MainBottomNav(navController = navController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            MainNavController(navigationController = navController, appointmentViewModel = appointmentViewModel)
        }
    }
}

@Composable
fun MainBottomNav(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar(Modifier.fillMaxWidth()) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "") },
            label = { Text("Citas") },
            selected = selectedItem == 0,
            onClick = {
                navController.navigate(Routes.Appointment.route)
                selectedItem = 0

            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "") },
            label = { Text("Clientes") },
            selected = selectedItem == 2,
            onClick = {
                navController.navigate(Routes.Client.route)
                selectedItem = 2
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "") },
            label = { Text("Buscar") },
            selected = selectedItem == 3,
            onClick = {
                navController.navigate(Routes.Search.route)
                selectedItem = 3
            }
        )
    }
}