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
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.albrodiaz.gestvet.R
import com.albrodiaz.gestvet.ui.features.home.views.navigation.MainNavController
import com.albrodiaz.gestvet.ui.features.home.views.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavState = rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    when (navStackEntry?.destination?.route) {
        Routes.Appointment.route, Routes.Search.route, Routes.Client.route -> {
            bottomNavState.value = true
        }
        else -> bottomNavState.value = false
    }

    Scaffold(
        bottomBar = { if (bottomNavState.value) MainBottomNav(
            navToHome = { navController.navigate(Routes.Appointment.route) },
            navToClients = { navController.navigate(Routes.Client.route){
                popUpTo(Routes.Appointment.route)
            } },
            navToSearch = { navController.navigate(Routes.Search.route){
                popUpTo(Routes.Appointment.route)
            } }
        ) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            MainNavController(
                navigationController = navController
            ) { available ->
                scope.launch {
                    snackbarHostState.showSnackbar(message = available, actionLabel = "OK")
                }
            }
        }
    }
}

/*TODO: Arreglar opción seleccionada por backstack entry*/
@Composable
fun MainBottomNav(
    navToHome: () -> Unit,
    navToClients: () -> Unit,
    navToSearch: () -> Unit
) {
    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar(Modifier.fillMaxWidth()) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "") },
            label = { Text(stringResource(id = R.string.appointments)) },
            selected = selectedItem == 0,
            onClick = {
                navToHome()
                selectedItem = 0

            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "") },
            label = { Text(stringResource(id = R.string.clients)) },
            selected = selectedItem == 2,
            onClick = {
                navToClients()
                selectedItem = 2
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "") },
            label = { Text(stringResource(id = R.string.search)) },
            selected = selectedItem == 3,
            onClick = {
                navToSearch()
                selectedItem = 3
            }
        )
    }
}