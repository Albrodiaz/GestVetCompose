package com.albrodiaz.gestvet.ui.features.home.views.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.home.navigation.MainNavController
import com.albrodiaz.gestvet.GestVetRoutes
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavState = rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    when (navStackEntry?.destination?.route) {
        GestVetRoutes.Appointment.route, GestVetRoutes.Search.route, GestVetRoutes.Client.route, GestVetRoutes.Settings.route -> {
            bottomNavState.value = true
        }
        else -> bottomNavState.value = false
    }

    val selectedItem = when (navStackEntry?.destination?.route) {
        GestVetRoutes.Client.route -> 1
        GestVetRoutes.Search.route -> 2
        GestVetRoutes.Settings.route -> 3
        else -> 0
    }

    Scaffold(
        bottomBar = {
            if (bottomNavState.value) MainBottomNav(
                selectedItem = selectedItem,
                navToHome = { navController.navigate(GestVetRoutes.Appointment.route) },
                navToClients = {
                    navController.navigate(GestVetRoutes.Client.route) {
                        popUpTo(GestVetRoutes.Appointment.route)
                    }
                },
                navToSearch = {
                    navController.navigate(GestVetRoutes.Search.route) {
                        popUpTo(GestVetRoutes.Appointment.route)
                    }
                },
                navToSettings = {
                    navController.navigate(GestVetRoutes.Settings.route) {
                        popUpTo(GestVetRoutes.Appointment.route)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            MainNavController(
                navigationController = navController,
                showMessage = { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(message = message)
                    }
                }
            ) { available ->
                scope.launch {
                    snackbarHostState.showSnackbar(message = available, actionLabel = "OK")
                }
            }
        }
    }
}

@Composable
private fun MainBottomNav(
    selectedItem: Int,
    navToHome: () -> Unit,
    navToSettings: () -> Unit,
    navToClients: () -> Unit,
    navToSearch: () -> Unit
) {
    NavigationBar(Modifier.fillMaxWidth()) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = stringResource(id = R.string.appointments)
                )
            },
            label = { Text(stringResource(id = R.string.appointments)) },
            selected = selectedItem == 0,
            onClick = { navToHome() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = stringResource(id = R.string.clients)
                )
            },
            label = { Text(stringResource(id = R.string.clients)) },
            selected = selectedItem == 1,
            onClick = { navToClients() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search)
                )
            },
            label = { Text(stringResource(id = R.string.search)) },
            selected = selectedItem == 2,
            onClick = { navToSearch() }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.settings)
                )
            },
            label = { Text(text = stringResource(id = R.string.settings)) },
            selected = selectedItem == 3,
            onClick = navToSettings
        )
    }
}